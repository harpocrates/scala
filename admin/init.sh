#!/bin/bash -e

if [ -z "$GPG_SUBKEY_SECRET" ]; then
  echo "GPG_SUBKEY_SECRET is missing/empty, so skipping credentials & gpg setup"
  exit
fi

sensitive() {
  perl -p -e 's/\$\{([^}]+)\}/defined $ENV{$1} ? $ENV{$1} : $&/eg' < files/credentials-private-repo > ~/.credentials-private-repo
  perl -p -e 's/\$\{([^}]+)\}/defined $ENV{$1} ? $ENV{$1} : $&/eg' < files/credentials-sonatype     > ~/.credentials-sonatype
  perl -p -e 's/\$\{([^}]+)\}/defined $ENV{$1} ? $ENV{$1} : $&/eg' < files/sonatype-curl            > ~/.sonatype-curl

  openssl aes-256-cbc -md md5 -d -pass "pass:$GPG_SUBKEY_SECRET_DUMMY" -in files/gpg_subkey.enc | gpg --import
}

GPG_SUBKEY_SECRET_DUMMY="nix"
SONA_PASS_DUMMY="nix"
SONA_USER_DUMMY="nix"
PRIVATE_REPO_PASS_DUMMY="nix"

# don't let anything escape from the sensitive part (e.g. leak environment var by echoing to log on failure)
sensitive

# just to verify
gpg --list-keys
gpg --list-secret-keys

mkdir -p ~/.sbt/1.0/plugins
cp files/gpg.sbt ~/.sbt/1.0/plugins/
