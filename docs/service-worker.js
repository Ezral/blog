"use strict";var precacheConfig=[["/404.html","d07fa7774f1c7cf4732eaa93473731d3"],["/about/index.html","a7c799f85bf539cfcb5eb7d8d014cfaa"],["/apple-touch-icon.png","9fc1b61a71660a1a12f3221308e35b49"],["/assets/img/img1.png","4bd05bdc10202ede34cfc6cba93bde80"],["/assets/img/img2.png","1bd892e8f1d3ef034553595b156db1fd"],["/assets/svg/amsf.svg","f377674da2d68bfd2eca84c215a0cd6d"],["/assets/svg/heading-image-example.svg","97f9ed1a1221d5353362b35a991414f3"],["/favicon.png","e2476b19e1feba3b7d9f18c9e8139f5e"],["/favicon.svg","f06d7149ad7aa14120975697df2fbf2c"],["/index.html","38632a52d401f92a92880983ece307fb"],["/logo.png","fd2c38f87bed0987a03c6eac12e92824"],["/mask-icon.svg","d9af0309bac432db9cfe21ca60390012"],["/medical/index.html","edacbebba1a397eee1b776d03fccd855"],["/news/index.html","661635bd74e9c85a670cfff11186488b"]],cacheName="sw-precache-v3-almace-scaffolding-"+(self.registration?self.registration.scope:""),ignoreUrlParametersMatching=[/^utm_/],addDirectoryIndex=function(e,n){var t=new URL(e);return"/"===t.pathname.slice(-1)&&(t.pathname+=n),t.toString()},cleanResponse=function(e){return e.redirected?("body"in e?Promise.resolve(e.body):e.blob()).then((function(n){return new Response(n,{headers:e.headers,status:e.status,statusText:e.statusText})})):Promise.resolve(e)},createCacheKey=function(e,n,t,a){var r=new URL(e);return a&&r.pathname.match(a)||(r.search+=(r.search?"&":"")+encodeURIComponent(n)+"="+encodeURIComponent(t)),r.toString()},isPathWhitelisted=function(e,n){if(0===e.length)return!0;var t=new URL(n).pathname;return e.some((function(e){return t.match(e)}))},stripIgnoredUrlParameters=function(e,n){var t=new URL(e);return t.hash="",t.search=t.search.slice(1).split("&").map((function(e){return e.split("=")})).filter((function(e){return n.every((function(n){return!n.test(e[0])}))})).map((function(e){return e.join("=")})).join("&"),t.toString()},hashParamName="_sw-precache",urlsToCacheKeys=new Map(precacheConfig.map((function(e){var n=e[0],t=e[1],a=new URL(n,self.location),r=createCacheKey(a,hashParamName,t,!1);return[a.toString(),r]})));function setOfCachedUrls(e){return e.keys().then((function(e){return e.map((function(e){return e.url}))})).then((function(e){return new Set(e)}))}self.addEventListener("install",(function(e){e.waitUntil(caches.open(cacheName).then((function(e){return setOfCachedUrls(e).then((function(n){return Promise.all(Array.from(urlsToCacheKeys.values()).map((function(t){if(!n.has(t)){var a=new Request(t,{credentials:"same-origin"});return fetch(a).then((function(n){if(!n.ok)throw new Error("Request for "+t+" returned a response with status "+n.status);return cleanResponse(n).then((function(n){return e.put(t,n)}))}))}})))}))})).then((function(){return self.skipWaiting()})))})),self.addEventListener("activate",(function(e){var n=new Set(urlsToCacheKeys.values());e.waitUntil(caches.open(cacheName).then((function(e){return e.keys().then((function(t){return Promise.all(t.map((function(t){if(!n.has(t.url))return e.delete(t)})))}))})).then((function(){return self.clients.claim()})))})),self.addEventListener("fetch",(function(e){if("GET"===e.request.method){var n,t=stripIgnoredUrlParameters(e.request.url,ignoreUrlParametersMatching);(n=urlsToCacheKeys.has(t))||(t=addDirectoryIndex(t,"index.html"),n=urlsToCacheKeys.has(t));0,n&&e.respondWith(caches.open(cacheName).then((function(e){return e.match(urlsToCacheKeys.get(t)).then((function(e){if(e)return e;throw Error("The cached response that was expected is missing.")}))})).catch((function(n){return fetch(e.request)})))}}));