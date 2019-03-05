#Regler for Monopoly
##Spil setup
###Spillere
Et spil består af 2-4 spillere, og to terninger.
Spillere starter med §1500

###Spillebræt
Et spillebræt består af 4 sider, med 10 felter pr side, så 40 felter i alt.
Hjørnefelterne er i rækkefølge:
1. Start
2. Fængsel/På besøg
3. Gratis Parkering
4. Gå i fængsel

På hver side er det 5. felt en togstation.
Det 4. felt på brættet er indkomstskat.
Det 2. sidste felt på brættet er topskat.

På hver side er der 1 prøv lykken felt og 1 chance felt.

De resterende felter på brættet er ejendom, og kan bygges på.
En side af brættet har 2 farvegrupper med ejendomme i. 
Jo længere man når på brættet jo højere er ejendomsprisen.

###Chancekort
Chancekort er risikable kort, der kan være dårlige eller positive. Der er 16 ialt af følgende typer:
* Gå i fængsel
* Gå til start
* Gå til _felt_
* Gå til _togstation_
* Gratis ud af fængsel
* Bank giver _beløb_
* Bank tager _beløb_

###Prøv lykken kort
'Prøv lykke' kort er kort der oftes giver en fordel til en spiller, der er 16 kort af følgende typer:
* Gratis ud af fængsel
* Gå til start
* Bank giver _beløb_
* Bank tager lille _beløb_
* \2. præmie i skøndhedskonkurrence modtag _beløb_

##Tur komposition
###Slå the terning
Slå to terninger, flyt det antal på terningen.
_Hvis_ dobbelt, slå igen, _hvis_ dobbelt 3 gange i streg, gå i fængsel.

###Når man har flyttet
Hvis man lander på noget uejet ejendom, så skal man gøre en af to ting:
1. Køb den
2. Sæt den på auktion.
    * Spillere skiftes til at byde, man skal byde mere end top budet, hvis `antal spillere - 1` ikke byder i streg, er det sidst højeste bud vinderen. Hvis ingen byder så er auktionen aflyst.

Hvis man lander på ejet ejendom, skal man betale `base husleje + (base husleje * antal huse) / 2`.
Hvis man lander på gå i fængsel, skal du i fængsel og din tur stopper med det samme.
Hivs du lander på gratis parkering sker der intet.
Hvis du lander på chance/Prøv lykken skal man tage et kort og adlyde det.

###Når man bygger
Når en spiller ejer __alle__ ejendomme i en farvegruppe, så må de bygge på de ejendomme. 
Når man bygger må der maks være 1 hus forskelv på den mindst og mest bebyggede ejendom.
Når man har bygget 5 huse bliver det automagisk til __et__ hotel, der må ikke være mere end __et__ hotel pr ejendom.
Der må maksimum bygges 32 huse og 12 hoteller på hele brættet i alt for alle spillere.
Når man sælger huse/hoteller sælges de til halv pris.

###Byttehandler
Alle ubebyggede ejedomme og togstationer, kan sælges til en anden spiller til hvilken som helst pris når deres tur er slut.
Hvis der er huse/hoteller på en ejendom skal de sælges til banken først.
En grund kan pantsættes til banken for halvdelen af dens værdi hvis der ikke er huse/hoteller på den, eller nogen andre i farvegruppen.
En pantsat grund kan ikke tage imod husleje. 
En pantsat grund kan købes tilbage af ejeren fra banken for `(pris / 2) * 1.1`.

##Endgame

###Konkurs
Hvis en spiller skylder mere til en spiller/bank end en spiller ejer, er de gået konkurs og ude af spillet.
Hvis spiller A går konkurs fordi de skulle betale spiller B, overtager spiller B alle spiller A´s likvide midler, og ejendom.
Den sidste spiller efter alle andre er gået konkurs, er vinderen.
