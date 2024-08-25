# Prosjekt TDT4100 Våren 2022

Prosjektet er et blackjack-spill. Brukeren spillet mot en maskin, som er dealeren. Dealeren sin oppførsel følger en stokastisk strategi, der den har en viss sannsynlighet for å trekke et kort ved hver enkelt verdi på hånden mellom 12-20. Disse sannsynlighetene endrer seg etter hvert som brukeren spiller flere og flere runder. Da endrer verdiene seg utifra om dealeren sin handling var "lur", som er om den trakk kort og ikke gikk over 21, eller om den ikke trakk, men vant runden. Resultat mot spilleren for hver enkelt runde har også en innvirkning på sannsynlighetene.

Spillet er laget i Java, med Maven og JavaFX.
