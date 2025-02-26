#!/usr/bin/env sh

set -eu

dir=$(mktemp -d)
createTemp() {
    mktemp -p "$dir"
}

caKey=$(createTemp)
openssl genpkey -algorithm RSA -out "$caKey"
caCert=$(createTemp)
openssl req -new -x509 -key "$caKey" -sha256 -days 365 -out "$caCert" -subj "/CN=MyCA"
privKey=$(createTemp)
openssl genpkey -algorithm RSA -out "$privKey"
csr=$(createTemp)
openssl req -new -key "$privKey" -out "$csr" -subj "/CN=Entity"
signed=$(createTemp)
openssl x509 -req -in "$csr" -CA "$caCert" -CAkey "$caKey" -CAcreateserial -out "$signed" -days 365 -sha256
openssl verify -CAfile "$caCert" "$signed"

mkdir -p src/main/resources/
mv -v "$caCert" src/main/resources/ca.crt
mv -v "$signed" src/main/resources/certificate.crt
rm -fr "$dir"
