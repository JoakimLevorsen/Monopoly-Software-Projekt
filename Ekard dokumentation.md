# Dokumentation til vores monopoly projekt _tada_
For at få adgang til vores source, klon vores github: <https://github.com/JoakimLevorsen/Monopoly-Software-Projekt>.

##Manglende features
Siden vi har besluttet at lave en clean slate implementering, betyder det at selve spillet ikke er klar til at kunne spille. Dog der er tests som `ModelTest` der opretter et spil, gemmer det i databasen, og sammenligner at det der så loades er struktureret korrekt. 

Når man kører spillet kommer der en dialog op der kan vise alle spil gemt i databasen. Den lader også en oprette et spil, efter man har indtastet et savename. Der er pt. ingen gem knapper, siden spillet gemmer automagisk undervejs.

##Opsætning af projektet
Til vores projekt bruger vi __ActiveJDBC__ som framework til at forbinde til databasen, dog for at det kan køre korrekt, skal man verificere projektstrukturen med maven først. Når maven-cli er installeret gøres det med:
`mvn verify`. Vi har googlet ihærdigt, og kan ikke finde ud af hvordan man gør det med IntelliJ.

Vi har vedhæftet et __SQL__ script til at opsætte databasestrukturen koden forventer. For at informere koden om placeringen af databasen, skal man redigere i `DatabaseBase.java`, der ligger i `src/main/java/monopoly/model/DatabaseBase.java`.