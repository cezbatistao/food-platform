#!/bin/sh -eu
if [ -z "${ENVIRONMENT_APP:-}" ]; then
    BRAND_JSON=undefined
else
    BRAND_JSON=$(jq -n --arg environment_app $ENVIRONMENT_APP '$environment_app')
fi

cat <<EOF
window.ENVIRONMENT_APP=$BRAND_JSON;
EOF