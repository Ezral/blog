"use strict";var precacheConfig=[["/404.html","f3a32d42c309cccc82d93675cca41407"],["/Almace-Scaffolding-quick-post.html","b36f40680fe0f4999b0ea40f93108536"],["/Guided-project-Mobile-App-for-lottery-addiction.html","75844bdd4baa08195b1129858ff93f4f"],["/Proportion-of-Doctors-in-Indonesia.html","b455341d73e12038e7daff26886409a6"],["/Reviewing-ECG.html","78e1551428a1b210816a91890b9ee1ee"],["/about/index.html","e34e381f39dfb7dbf3b44f4e88c950bb"],["/air-medical-evacuation.html","f4e1127e23d578f989d727a0b7a38d1a"],["/android-chrome-192x192.png","4ad60aecb63bdfab4cad33b52d5097f2"],["/android-chrome-512x512.png","f07c3c38312427db4a533936a6a575c9"],["/apple-touch-icon.png","1ce721896a851646d058a2c2225d18df"],["/assets/img/img1.png","4bd05bdc10202ede34cfc6cba93bde80"],["/assets/img/img2.png","1bd892e8f1d3ef034553595b156db1fd"],["/assets/svg/amsf.svg","f377674da2d68bfd2eca84c215a0cd6d"],["/assets/svg/heading-image-example.svg","97f9ed1a1221d5353362b35a991414f3"],["/data-quest-guided-project.html","a978aec57706c8f642b2eaf49bba3b2c"],["/data/index.html","4d3d0d36833176e0afe40f0834c338b4"],["/doctor-proportion.html","7513938f87e4d6ed8c716bd116aad987"],["/favicon-16x16.png","7ba2d2b346ba3444dbd8c6eecab17c3b"],["/favicon-32x32.png","bfd2baf3cc94a977966f24b719d1375a"],["/favicon.png","f9ebc5b37a6d03e75d12097ca919561c"],["/favicon.svg","d421f54bfbe396a2c122c374a3edb77c"],["/index.html","e21c9bf3bddc992d97945dd21aba855c"],["/logo.png","fd2c38f87bed0987a03c6eac12e92824"],["/mask-icon.svg","042604fd5e8bdfa5d9ac053a8485e10b"],["/medical/index.html","b03e1a2112e8735fce15a1bbf7a92ba0"],["/news/index.html","26986bbdfcaf6959ad97e2ca5a960e2e"],["/setting-up-zsh.html","d65fdcfbc8b5a5bda8487a2e7e0de004"]],cacheName="sw-precache-v3-almace-scaffolding-"+(self.registration?self.registration.scope:""),ignoreUrlParametersMatching=[/^utm_/],addDirectoryIndex=function(e,a){var t=new URL(e);return"/"===t.pathname.slice(-1)&&(t.pathname+=a),t.toString()},cleanResponse=function(e){return e.redirected?("body"in e?Promise.resolve(e.body):e.blob()).then((function(a){return new Response(a,{headers:e.headers,status:e.status,statusText:e.statusText})})):Promise.resolve(e)},createCacheKey=function(e,a,t,n){var r=new URL(e);return n&&r.pathname.match(n)||(r.search+=(r.search?"&":"")+encodeURIComponent(a)+"="+encodeURIComponent(t)),r.toString()},isPathWhitelisted=function(e,a){if(0===e.length)return!0;var t=new URL(a).pathname;return e.some((function(e){return t.match(e)}))},stripIgnoredUrlParameters=function(e,a){var t=new URL(e);return t.hash="",t.search=t.search.slice(1).split("&").map((function(e){return e.split("=")})).filter((function(e){return a.every((function(a){return!a.test(e[0])}))})).map((function(e){return e.join("=")})).join("&"),t.toString()},hashParamName="_sw-precache",urlsToCacheKeys=new Map(precacheConfig.map((function(e){var a=e[0],t=e[1],n=new URL(a,self.location),r=createCacheKey(n,hashParamName,t,!1);return[n.toString(),r]})));function setOfCachedUrls(e){return e.keys().then((function(e){return e.map((function(e){return e.url}))})).then((function(e){return new Set(e)}))}self.addEventListener("install",(function(e){e.waitUntil(caches.open(cacheName).then((function(e){return setOfCachedUrls(e).then((function(a){return Promise.all(Array.from(urlsToCacheKeys.values()).map((function(t){if(!a.has(t)){var n=new Request(t,{credentials:"same-origin"});return fetch(n).then((function(a){if(!a.ok)throw new Error("Request for "+t+" returned a response with status "+a.status);return cleanResponse(a).then((function(a){return e.put(t,a)}))}))}})))}))})).then((function(){return self.skipWaiting()})))})),self.addEventListener("activate",(function(e){var a=new Set(urlsToCacheKeys.values());e.waitUntil(caches.open(cacheName).then((function(e){return e.keys().then((function(t){return Promise.all(t.map((function(t){if(!a.has(t.url))return e.delete(t)})))}))})).then((function(){return self.clients.claim()})))})),self.addEventListener("fetch",(function(e){if("GET"===e.request.method){var a,t=stripIgnoredUrlParameters(e.request.url,ignoreUrlParametersMatching);(a=urlsToCacheKeys.has(t))||(t=addDirectoryIndex(t,"index.html"),a=urlsToCacheKeys.has(t));0,a&&e.respondWith(caches.open(cacheName).then((function(e){return e.match(urlsToCacheKeys.get(t)).then((function(e){if(e)return e;throw Error("The cached response that was expected is missing.")}))})).catch((function(a){return fetch(e.request)})))}}));