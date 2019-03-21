# Author noter
### Når man laver Author noter strukureres det som følgende:
```Java
/*
METODENAVN:
HVAD GØR DEN

@author NAVN, STUDIENUMMER
@author NAVN PÅ PERSON DER RETTEDE, STUDIENUMMER
@author NAVN PÅ PERSON DER RETTEDE IGEN, STUDIENUMMER
*/
```


# Regler for Monopoly
## Spil setup
### Spillere
Et spil består af 2-4 spillere og to terninger.
Spillere starter med §1500 og modtager §200 hver gang de passerer Start.

### Spillebræt
Et spillebræt består af 4 sider med 10 felter pr. side, dvs. 40 felter i alt.
Hjørnefelterne er i rækkefølge (i urets retning):
1. Start
2. Fængsel/På besøg
3. Gratis Parkering
4. Gå i fængsel

På hver side er det 5. felt en togstation.
Det 4. felt på brættet er Indkomstskat.
Det 2. sidste felt på brættet er Topskat.

På hver side er der 1 Prøv Lykken felt og 1 Chance felt.

De resterende felter på brættet er ejendom, og kan bygges på.
En side af brættet har 2 farvegrupper med ejendomme i. 
Jo længere man når på brættet jo højere er ejendomsprisen.

### Chancekort
Chancekort er risikable kort, der kan være dårlige eller positive. Der er 16 ialt af følgende typer:
* Gå i fængsel
* Gå til start
* Gå til _felt_
* Gå til _togstation_
* Gratis ud af fængsel
* Bank giver _beløb_
* Bank tager _beløb_

### Prøv lykken kort
'Prøv lykken' kort giver oftest en fordel til en spiller, der er 16 kort af følgende typer:
* Gratis ud af fængsel
* Gå til start
* Bank giver _beløb_
* Bank tager lille _beløb_
* \2. præmie i skøndhedskonkurrence modtag _beløb_

## Tur komposition
### Slå terning
Slå to terninger og flyt det antal på terningen.
_Hvis_ dobbelt, slå igen, _hvis_ dobbelt 3 gange i streg, gå i fængsel.

### Fængsel
Hvis man er i fængsel skal man starte med at slå for at komme ud.
Hvis man slår dobbelt er man løsladt, og får en almindelig tur.
Hvis man betaler sig ud for __§100__ får man også en almindelig tur, istedet for at betale kan man bruge et "Gratis ud af fængsel kort".
Efter 3 ture i fængsel skal man betale sig ud.

### Når man har flyttet
Hvis man lander på en ejendom uden en ejer, så skal man gøre en af to ting:
1. Købe den
2. Sætte den på auktion.
    * Spillere skiftes til at byde, man skal byde mere end top budet, hvis `antal spillere - 1` ikke byder i streg, er det sidst højeste bud vinderen. Hvis ingen byder så er auktionen aflyst.

Hvis man lander på en ejet ejendom, skal man betale `base husleje + (base husleje * antal huse) / 2`.
Hvis man lander på Gå i Fængsel, skal man fængsles og turen slutter med det samme.
Hivs man lander på Gratis Parkering får man pengene, der ligger på feltet. Hver gang en spiller betaler topskat eller indkomstskat lægges penge på Gratis Parkering.
Hvis man lander på Chance/Prøv Lykken skal man tage et kort og handle derefter.

### Når man bygger
Når en spiller ejer __alle__ ejendomme i en farvegruppe, så må de bygge på de ejendomme. 
Når man bygger må der maks være 1 hus forskelv på den mindst og mest bebyggede ejendom.
Når man har bygget 5 huse bliver det automagisk til __et__ hotel, der må ikke være mere end __et__ hotel pr ejendom.
Der må maksimum bygges 32 huse og 12 hoteller på hele brættet i alt for alle spillere.
Når man sælger huse/hoteller sælges de til halv pris.

### Byttehandler
Alle ubebyggede ejedomme og togstationer, kan sælges til en anden spiller til hvilken som helst pris når deres tur er slut.
Hvis der er huse/hoteller på en ejendom skal de sælges til banken først.
En grund kan pantsættes til banken for halvdelen af dens værdi hvis der ikke er huse/hoteller på den, eller nogen andre i farvegruppen.
En pantsat grund kan ikke tage imod husleje. 
En pantsat grund kan købes tilbage af ejeren fra banken for `(pris / 2) * 1.1`.

## Endgame

### Konkurs
Hvis en spiller skylder mere til en anden spiller/banken end vedkommened ejer, er de gået konkurs og ude af spillet.
Hvis spiller A går konkurs fordi de skulle betale spiller B, overtager spiller B alle spiller A´s likvide midler, og ejendomme.
Den spiller, der står tilbage efter alle andre er gået konkurs, er vinderen.
