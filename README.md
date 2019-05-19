# Author noter
### Når man laver Author noter strukureres det som følgende:
```Java
/**
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
Spillere starter med §2000 og modtager §200 hver gang de passerer Start.

### Spillebræt
Et spillebræt består af 4 sider med 10 felter pr. side, dvs. 40 felter i alt.
Hjørnefelterne er i rækkefølge (i urets retning):
1. Start
2. Fængsel/På besøg
3. Gratis Parkering
4. Gå i fængsel

På hver side er det 6. felt en togstation og der er 1 Prøv Lykken felt og 1 Chance felt.
Det 4. felt på brættet er Indkomstskat og det næstsidste felt på brættet er Topskat.
De resterende felter på brættet er ejendomme, som kan bygges på. Togstationer kan også ejes, men må ikke bygges på.
Hver side af brættet har 2 farvegrupper med ejendomme.
Jo længere man når på brættet jo højere er ejendomsprisen.

### Chancekort
__Chancekort__ er risikable kort, der kan være dårlige eller positive. Der er 16 i alt af følgende typer:
* Gå i fængsel
* Gratis ud af fængsel
* Gå til _felt_
* Bank giver/tager _beløb_

### Prøv lykken kort
__Prøv lykken__ kort giver oftest en fordel til en spiller, og der er 16 kort af følgende typer:
* Gratis ud af fængsel
* Gå til _felt_
* Bank giver _beløb_/tager lille _beløb_

## Tur komposition
### Fængsel
Hvis man er i fængsel skal man starte med at slå for at komme ud.
Hvis man slår dobbelt er man løsladt, og får en almindelig tur.
Hvis man betaler sig ud for __§100__ får man også en almindelig tur. Hvis man har et "Gratis ud af fængsel"-kort, kan man bruge dette i stedet for at betale.

### Slå terning
Slå to terninger og flyt det antal felter, som svarer til terningernes øjne.
_Hvis_ dobbelt, slå igen.

### Når man har flyttet
Hvis man lander på en ejendom/station uden en ejer, så skal man gøre en af to ting:
1. Købe den
2. Sætte den på auktion.
    * Spillere skiftes til at byde, og skal byde mere end topbudet. Hvis `antal spillere - 1` ikke byder i streg, er det sidste topbud vinderen. Hvis ingen byder så er auktionen aflyst og banken beholder ejendommen.

Hvis man lander på en ejet ejendom, skal man betale `base husleje + (base husleje * antal huse) / 2`.
Hvis man lander på en ejet station, skal man betale `base husleje * antal stationer ejeren ejer`.
Hvis man lander på __Gå i Fængsel__, skal man fængsles og turen slutter med det samme.
Hvis man lander på __Topskat__ eller __Indkomstskat__ skal spilleren betale hhv. __§100__/__§200__ til banken.
Hvis man lander på __Gratis Parkering__ får man pengene, der ligger på feltet. Hver gang en spiller betaler topskat eller indkomstskat lægges pengene på Gratis Parkering.
Hvis man lander på __Chance__/__Prøv Lykken__ skal man tage et kort og handle derefter.

### Når man bygger
Når en spiller ejer __alle__ ejendomme i en farvegruppe og de ikke er pantsat, så må de bygge på de ejendomme.
Når man har bygget 5 huse bliver det automatisk til __et__ hotel. Der må ikke være mere end __et__ hotel pr ejendom.
Når man sælger huse/hoteller sælges de til halv pris.

### Byttehandler
En spiller kan sælge alle ubebyggede ejendomme og togstationer til en anden spiller til hvilken som helst pris når deres tur er slut, så længe den anden spiller acceptere. De kan ligeledes købe en ubebygget ejendom fra en anden spiller.
Hvis der er huse/hoteller på en ejendom skal de sælges til banken først.

### Pantsættelse
En grund kan pantsættes til banken for halvdelen af dens pris hvis der ikke er huse/hoteller på den.
En pantsat grund kan ikke tage imod husleje.
En pantsat grund kan købes tilbage af ejeren fra banken for `(pris / 2) * 1.1`.

## Endgame

### Konkurs
Hvis en spillers balance er negativ når spillerens tur slutter, startes konkurshåndtering, hvor spilleren har mulighed for at sælge eller pantsætte ejendomme for at redde sig selv. Er spillerens balance fortsat negativ efter konkurshåndteringen, er de gået konkurs og er ude af spillet.
Den spiller, der står tilbage efter alle andre er gået konkurs, er vinderen.