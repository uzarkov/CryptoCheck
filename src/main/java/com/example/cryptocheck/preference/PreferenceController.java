package com.example.cryptocheck.preference;

import com.example.cryptocheck.preference.dto.PreferenceInput;
import com.example.cryptocheck.preference.dto.PreferenceOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preference")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PreferenceOutput>> getUserPreferences(Principal principal) {
        var userEmail = principal.getName();
        var preferences = preferenceService.getUserPreferences(userEmail);
        return ResponseEntity.ok().body(preferences);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PreferenceOutput> addPreference(@RequestBody PreferenceInput preferenceInput,
                                                          Principal principal) {
        var userEmail = principal.getName();
        var preference = preferenceService.addPreference(preferenceInput, userEmail);

        return ResponseEntity.ok().body(preference);
    }

    @DeleteMapping("/{cryptocurrencyName}")
    public ResponseEntity<Void> removePreference(@PathVariable String cryptocurrencyName,
                                                            Principal principal) {
        var userEmail = principal.getName();
        preferenceService.removePreference(cryptocurrencyName, userEmail);
        return ResponseEntity.ok().build();
    }
}
