#!/bin/sh

cd

timestamp() {
    date -u +"%Y-%m-%dT%H:%M:%SZ"
}

log () {
    echo $1
    echo "$(timestamp): $1" >> build.log
}

if [ ! -d "zombieclj.no" ]; then
    git clone https://github.com/magnars/zombieclj.no.git
    changed=1
fi

cd zombieclj.no

if [ -f "in-progress.tmp" ]; then
    log "Build in progress, aborting"
else
    touch in-progress.tmp

    git pull | grep -q -v 'Already up-to-date.' && changed=1

    if [ $changed ]; then
        log "Building"
        ../bin/lein with-profile zclj build-site && built=1
        if [ $built ]; then
            log "Publishing"
            rm -rf /var/www/zombieclj.no/current
            mv build /var/www/zombieclj.no/current
            log "Purging cache"
            varnishadm -S /etc/varnish/secret -T localhost:6082 "purge req.url ~ (/|[.]html)$"
            log "Done!"
        else
            log "Build failed, aborting."
        fi
    else
        log "Site is up to date."
    fi

    rm in-progress.tmp
fi
