package com.example.cryptocheck.position;

import com.example.cryptocheck.position.dto.PositionClosure;
import com.example.cryptocheck.position.dto.PositionInput;
import com.example.cryptocheck.position.dto.PositionOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/position")
public class PositionController {

    private final PositionService positionService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PositionOutput>> findAllPositions(Principal principal,
                                                                 Pageable pageable) {
        var userEmail = principal.getName();
        var positions = positionService.findAllPositionsByUserEmail(userEmail, pageable);

        return ResponseEntity.ok().body(positions);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PositionOutput> addPosition(@RequestBody PositionInput position,
                                                      Principal principal) {
        var userEmail = principal.getName();
        var newPosition = positionService.addPosition(position, userEmail);

        return ResponseEntity.ok().body(newPosition);
    }

    @DeleteMapping(value = "/{positionId}")
    public ResponseEntity<Void> removePosition(@PathVariable Long positionId,
                                               Principal principal) {
        var userEmail = principal.getName();
        positionService.removePosition(positionId, userEmail);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PositionOutput> close(@RequestBody PositionClosure positionClosure,
                                                Principal principal) {
        var userEmail = principal.getName();
        var closedPosition = positionService.close(positionClosure, userEmail);

        return ResponseEntity.ok().body(closedPosition);
    }
}
