# Zombie CLJ

Koden bak www.zombieclj.no

> Zombier, Mafia og Clojure.
> Også to tomsinger fra Østfold.

## Kjøre lokalt

Skaff [leiningen](https://github.com/technomancy/leiningen#leiningen)
om du ikke har den. Sats på versjon 2.3+. Hvis du har en gammel
versjon under 2.0 så funker det garantert ikke.

Du må også ha JDK 1.7. Sjekk med `java -version`, ellers
[last ned her](http://docs.oracle.com/javase/7/docs/webnotes/install/index.html).

Gå så til rota av prosjektet, og

```shell
lein ring server-headless
```

Voila!
