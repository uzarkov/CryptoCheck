package com.example.cryptocheck.position;

import com.example.cryptocheck.cryptocurrency.CryptocurrencyService;
import com.example.cryptocheck.exception.general.UnauthorizedException;
import com.example.cryptocheck.helper.AssetsHelper;
import com.example.cryptocheck.position.dto.PositionClosure;
import com.example.cryptocheck.position.dto.PositionInput;
import com.example.cryptocheck.position.dto.PositionOutput;
import com.example.cryptocheck.position.exception.NoSuchPositionException;
import com.example.cryptocheck.price.PriceService;
import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PositionService {

   private final PositionRepository positionRepository;
   private final AppUserService userService;
   private final PriceService priceService;
   private final CryptocurrencyService cryptocurrencyService;

   public Page<PositionOutput> findAllPositionsByUserEmail(String email,
                                                           Pageable pageable) {
       var user = userService.getUserByEmail(email);
       return findAllPositionsByUser(user, Map.of(), pageable);
   }

   public Page<PositionOutput> findAllPositionsByUser(AppUser appUser,
                                                      Map<String, BigDecimal> prices,
                                                      Pageable pageable) {
       var positions = positionRepository.findAllByAppUser(appUser, pageable);
       if (prices.isEmpty()) {
           prices = getRequiredPrices(positions);
       }
       var finalPrices = prices;

       return positions.map(pos -> buildPosition(pos, finalPrices.get(pos.getCryptocurrency().getSymbol())));
   }

   @Transactional
   public PositionOutput addPosition(PositionInput positionInput,
                                     String userEmail) {
       PositionValidator.validateNewPosition(positionInput);
       var user = userService.getUserByEmail(userEmail);
       var cryptoName = positionInput.cryptocurrencyName();
       var cc = cryptocurrencyService.getCryptocurrencyById(cryptoName);
       var position = positionRepository.save(positionInput.toPosition(user, cc));
       var currPrice = priceService.getCurrentPriceOf(cc.getSymbol());

       return buildPosition(position, new BigDecimal(currPrice));
   }

   private PositionOutput buildPosition(Position position,
                                        BigDecimal currentPrice) {
       var closurePrice = position.getClosurePrice();
       var finalPrice = Objects.isNull(closurePrice) ? currentPrice : closurePrice;
       var percentageChange = AssetsHelper.computePercentageChange(position.getEntryPrice(), finalPrice);

       return PositionOutput.from(position, currentPrice, percentageChange);
   }

   @Transactional
   public void removePosition(Long positionId,
                              String userEmail) {
       var position = findFirstPositionById(positionId);

       if (isUserPositionOwner(userEmail, position)) {
           positionRepository.delete(position);
           return;
       }
       throw UnauthorizedException.unauthorized();
   }

   @Transactional
   public PositionOutput close(PositionClosure positionClosure,
                               String userEmail) {
       var targetPosition = findFirstPositionById(positionClosure.id());
       if (isUserPositionOwner(userEmail, targetPosition)) {
           PositionValidator.validatePositionClosure(targetPosition, positionClosure);
           targetPosition.setClosurePrice(BigDecimal.valueOf(positionClosure.closurePrice()));
           targetPosition.setClosureTime(positionClosure.closureDate());
           var closedPosition = positionRepository.save(targetPosition);
           var currentPrice = priceService.getCurrentPriceOf(
                   targetPosition.getCryptocurrency().getSymbol());
           return buildPosition(closedPosition, new BigDecimal(currentPrice));
       }
       throw UnauthorizedException.unauthorized();
   }

   private Position findFirstPositionById(Long id) {
       return positionRepository
               .findFirstById(id)
               .orElseThrow(NoSuchPositionException::invalidId);
   }

   private boolean isUserPositionOwner(String userEmail,
                                       Position position) {
       System.out.println("Position email: " + "x" + position.getAppUser().getEmail() +"x+ " + "\nsec email: " + "x" +userEmail + "x");
       System.out.println(position.getAppUser().getEmail().equals(userEmail));
       return position.getAppUser().getEmail().equals(userEmail);
   }

   private Map<String, BigDecimal> getRequiredPrices(Page<Position> positions) {
       var symbols = positions.stream()
               .map(position -> position.getCryptocurrency().getSymbol())
               .distinct()
               .collect(Collectors.joining(","));

       if (symbols.isEmpty()) {
           return Map.of();
       }

       return priceService
               .getCurrentPricesOf(symbols)
               .entrySet()
               .stream()
               .map(e -> new AbstractMap.SimpleEntry<>(
                            e.getKey(),
                            new BigDecimal(e.getValue())
               ))
               .collect(Collectors.toMap(
                       Map.Entry::getKey,
                       Map.Entry::getValue
               ));
   }
}
