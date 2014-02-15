## Provisjonering

Vi bruker [Ansible](www.ansibleworks.com) for å sette opp serveren.
Hvis du sitter på OSX er det så enkelt som `brew install ansible`. Da
får du `1.4.3` eller nyere, noe du også trenger.

### Sette opp din egen server lokalt

Du kan bruke [Vagrant](http://www.vagrantup.com/) og
[VirtualBox](https://www.virtualbox.org/) for å sette opp en virtuell
blank CentOS server lokalt.

```sh
cd provisioning/devbox
vagrant plugin install vagrant-vbguest
vagrant up
echo "\n192.168.33.45 local.zombieclj.no" | sudo tee -a /etc/hosts
```

Det er mulig du får en `An error occurred during installation of
VirtualBox Guest Additions. Some functionality may not work as
intended.` ... det er ikke stress. Bare "Window System drivers" som
ikke blir installert.

Deretter må du sette passord for root. Sudo passord er `kodemaker`:

```sh
vagrant ssh
sudo passwd root
```

Logg ut igjen.

Legg til din public key i `provisioning/keys`, og føy den til listen
under `Setup authorized_keys for users who may act as deploy user`
tasken i `provisioning/bootstrap.yml`.

Gå så tilbake til `provisioning/` og:

```sh
ansible-playbook -i hosts.ini bootstrap.yml --user root --ask-pass
```

Svar med passordet du lagde til root.

Den kjører en god stund, og så kan du `ssh deploy@local.zombieclj.no`
og se deg omkring.

Fortsett så til [Sette opp zombieclj.no](#neste-sette-opp-zombiecljno).

### Provisjonere en server

Så, du har en fresk og fersk CentOS server som vil bli zombieclj.no.
Legg den til i `provisioning/hosts.ini` under `[new-servers]`. Du kan
ta bort `192.168.33.45`, den brukes bare for lokal testing.

Forhåpentligvis har du testet lokalt, og dermed ligger allerede din
public key i `provisioning/keys`.

Så gjenstår det bare å gå til `provisioning/` katalogen og inkantere:

```sh
ansible-playbook -i hosts.ini bootstrap.yml --user root --ask-pass
```

#### Øhh, det gikk ikke helt bra

Nei, du mangler kanskje `sshpass` lokalt hos deg? Det er bare en yum
eller apt unna. Eller hvis du er på OSX:

```sh
brew install https://raw.github.com/eugeneoden/homebrew/eca9de1/Library/Formula/sshpass.rb
```

### Neste: Sette opp zombieclj.no

Når du bootstrapper, så vil root-login og passord-login bli disablet.
Så når vi nå skal sette opp zombieclj no, så må du fleske til med en
annen inkantasjon:

```sh
ansible-playbook -i hosts.ini setup-zombieclj.yml --user deploy --sudo --ask-sudo-pass
```

Nå er det altså ikke SSH-passordet som brukes lenger - den bruker din
private key - men du må oppgi sudo-passordet. Dersom du ikke har gjort
noen endringer, så er det fortsatt `kodemaker`. Men hvis dette er en
offentlig server, så lønner det seg nok å gjøre den endringen. Logg
inn som `deploy` og `passwd`.

#### Bygg siten

Første gang du bygger tar det lang tid. Det kan være hyggelig å se at
den holder på med noe.

```sh
ssh deploy@local.zombieclj.no
./build-site.sh
```

Og så kan du besøke http://local.zombieclj.no i nettleseren din og
meske deg i de nye sidene våre.

Når du vil oppgradere, kan du be serveren om å bygge en ny versjon av
siten:

```sh
curl local.zombieclj.no/site/build
```

Den henter da altså fra github. Om du vil teste lokale endringer er
det mye greiere å få til med `lein ring server`.
