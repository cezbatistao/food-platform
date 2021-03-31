# FROM node:alpine as builder

# WORKDIR /app
# COPY package.json .
# COPY package-lock.json .
# RUN npm ci

# ## Set default OS_ENV
# ARG REACT_APP_ENVIRONMENT=hml
# ENV REACT_APP_ENVIRONMENT $REACT_APP_ENVIRONMENT

# COPY . .
# RUN npm run build

# FROM nginx:1.19.0-alpine

# RUN rm /etc/nginx/conf.d/default.conf

# ## Copy our default nginx config template
# COPY --from=builder /app/nginx/default.template /etc/nginx/conf.d/

# ## Remove default nginx website
# RUN rm -rf /usr/share/nginx/html/*

# COPY --from=builder /app/build /usr/share/nginx/html

# ## Copy proxy env configs
# RUN mkdir -p /config/nginx/
# COPY --from=builder /app/nginx/proxy-*.conf /config/nginx/

# # support running as arbitrary user which belongs to the root group
# RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx && \ 
#   chgrp -R 0 /etc/nginx/conf.d && \ 
#   chmod -R g=u /etc/nginx/conf.d && \ 
#   sed -i.bak 's/^user/#user/' /etc/nginx/nginx.conf && \ 
#   addgroup nginx root

# USER nginx

# ARG PROXY_ENV=hml
# ARG PROXY_ENV $PROXY_ENV

# EXPOSE 80

# CMD envsubst '$PROXY_ENV' < /etc/nginx/conf.d/default.template > /etc/nginx/conf.d/default.conf \
#   && exec nginx -g 'daemon off;'

FROM nginx:1.19.0-alpine

RUN apk add --no-cache jq && \
  rm /etc/nginx/conf.d/default.conf && \
  rm -rf /usr/share/nginx/html/*

## Copy our default nginx config template
COPY nginx/default.template /etc/nginx/conf.d/

## Remove default nginx website
#RUN rm -rf /usr/share/nginx/html/*

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
