FROM nginx:1.19.0-alpine

RUN apk add --no-cache jq && \
  rm /etc/nginx/conf.d/default.conf && \
  rm -rf /usr/share/nginx/html/*

## Copy our default nginx config template
COPY nginx/default.template /etc/nginx/conf.d/

COPY build /usr/share/nginx/html

ARG ENVIRONMENT_APP=hml
ENV ENVIRONMENT_APP $ENVIRONMENT_APP

COPY generate_config_js.sh /
RUN touch /usr/share/nginx/html/config.js && \
  chmod 775 generate_config_js.sh /usr/share/nginx/html/config.js && \
  mkdir -p /config/nginx/

## Copy proxy env configs
COPY nginx/proxy-*.conf /config/nginx/

# support running as arbitrary user which belongs to the root group
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx && \ 
  chgrp -R 0 /etc/nginx/conf.d && \ 
  chmod -R g=u /etc/nginx/conf.d && \ 
  sed -i.bak 's/^user/#user/' /etc/nginx/nginx.conf && \ 
  addgroup nginx root

USER nginx

ARG PROXY_ENV=hml
ARG PROXY_ENV $PROXY_ENV

EXPOSE 80

CMD envsubst '$PROXY_ENV' < /etc/nginx/conf.d/default.template > /etc/nginx/conf.d/default.conf \
  && ./generate_config_js.sh > /usr/share/nginx/html/config.js && exec nginx -g 'daemon off;'
