#!/bin/sh

# Decrypt the files

gpg --quiet --batch --yes --decrypt --passphrase="$SECRETS_DECRYPT_PASSPHRASE" \
--output /app/google-services.json google-services.json.gpg

gpg --quiet --batch --yes --decrypt --passphrase="$SECRETS_DECRYPT_PASSPHRASE" \
--output /src/main/res/values/secrets.xml secrets.xml.gpg
