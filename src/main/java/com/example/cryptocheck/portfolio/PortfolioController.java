package com.example.cryptocheck.portfolio;

import com.example.cryptocheck.portfolio.dto.PortfolioOutput;
import com.example.cryptocheck.portfolio.dto.PortfolioRecordInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PortfolioOutput> getPortfolio(Principal principal) {
        var userEmail = principal.getName();
        var portfolio = portfolioService.getPortfolio(userEmail);

        return ResponseEntity.ok().body(portfolio);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PortfolioOutput> addPortfolioRecord(@RequestBody PortfolioRecordInput portfolioRecord,
                                                              Principal principal) {
        var userEmail = principal.getName();
        var updatedPortfolio = portfolioService.addPortfolioRecord(portfolioRecord, userEmail);
        return ResponseEntity.ok().body(updatedPortfolio);
    }


}
