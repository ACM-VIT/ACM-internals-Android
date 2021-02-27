#!/bin/sh

# Decrypt the files

gpg --quiet --batch --yes --decrypt --passphrase="$SECRETS_DECRYPT_PASSPHRASE" \
--output /app/google-services.json .github/Encrypted\ Secrets/google-services.json.gpg

gpg --quiet --batch --yes --decrypt --passphrase="$SECRETS_DECRYPT_PASSPHRASE" \
--output /src/main/res/values/secrets.xml .github/Encrypted\ Secrets/secrets.xml.gpg
