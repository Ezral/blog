"use strict";var precacheConfig=[["/404.html","6d35b671670823730a9947d6e2f97002"],["/Almace-Scaffolding-quick-post.html","c263aca9f86ed0c3447b2bd6145c8d26"],["/Best-coffee-in-Bogor.html","55fe2dbbf5bfd18ed4de549a11f93062"],["/Brazilian-jiu-jitsu.html","f8a05c3c9a9272db804c6cb4dc8e268a"],["/Coffee-making-a-cup.html","7230d28b3d4ec5b3fc5667cb727f98f7"],["/Dataquest-guided-project-3.html","634d3f8a6ca4b0f145be4187895a3489"],["/Displaying-math-formula-on-ASMF.html","0800ddd28673d7566e770fb3de4b81fd"],["/Framingham-risk-score.html","29b37da18d1cc2345ddf294942624d60"],["/Guided-project-Mobile-App-for-lottery-addiction.html","e547ba90d97ee0d0b385763b938b58b5"],["/Parsing-ICD10PCS-XML.html","89933c0a096becb88a843f6c13df577a"],["/Percentile-of-score.html","708377f6dac7636eae247f923e8e98bd"],["/Proportion-of-Doctors-in-Indonesia.html","f002b5a1a2c954455528dd4129b15235"],["/Reviewing-ECG.html","dbc34025342e06539f2e2c5e8622d793"],["/Setting-up-blog-using-Leonids.html","7dedae62760b9e58d2d3f5f9ae92387d"],["/about/index.html","24277df05ffe95ac8987ddf4083b9fd5"],["/air-medical-evacuation.html","ed4ca3e845cc2a00901705abd9f08612"],["/android-chrome-192x192.png","4ad60aecb63bdfab4cad33b52d5097f2"],["/android-chrome-512x512.png","f07c3c38312427db4a533936a6a575c9"],["/apple-touch-icon.png","1ce721896a851646d058a2c2225d18df"],["/assets/img/cohere01.jpg","a2571e7e7a0b2c98a16de343228181a7"],["/assets/img/cohere02.jpg","57105771fbba4de5c8dbc4ad30f39108"],["/assets/img/cohere05.jpg","919e5f4b0bdc637eb5230e7e352e45bf"],["/assets/img/img1.png","4bd05bdc10202ede34cfc6cba93bde80"],["/assets/img/img2.png","1bd892e8f1d3ef034553595b156db1fd"],["/assets/img/snippet.jpg","4299d88d901aa4fe37f2bc4408f583f4"],["/assets/svg/amsf.svg","f377674da2d68bfd2eca84c215a0cd6d"],["/assets/svg/heading-image-example.svg","97f9ed1a1221d5353362b35a991414f3"],["/data-quest-guided-project.html","79d3dc459571040c41826a6de696b10d"],["/data/index.html","ab098256be9ac4a3bb663c8b2104e759"],["/doctor-proportion.html","e548247bb45ad7cffaf0427f90dd3ff4"],["/favicon-16x16.png","7ba2d2b346ba3444dbd8c6eecab17c3b"],["/favicon-32x32.png","bfd2baf3cc94a977966f24b719d1375a"],["/favicon.png","f9ebc5b37a6d03e75d12097ca919561c"],["/favicon.svg","d421f54bfbe396a2c122c374a3edb77c"],["/index.html","e2f41dec9c57e227597c30cfa347f319"],["/life/index.html","17f9a3ebcb393bfbd97abe16efc678cf"],["/logo.png","fd2c38f87bed0987a03c6eac12e92824"],["/mask-icon.svg","042604fd5e8bdfa5d9ac053a8485e10b"],["/medical/index.html","9e20a7c5f43607a7bd83f02d198725b6"],["/setting-up-zsh.html","603c2abae46b60195440747e58791bcf"]],cacheName="sw-precache-v3-almace-scaffolding-"+(self.registration?self.registration.scope:""),ignoreUrlParametersMatching=[/^utm_/],addDirectoryIndex=function(e,a){var t=new URL(e);return"/"===t.pathname.slice(-1)&&(t.pathname+=a),t.toString()},cleanResponse=function(e){return e.redirected?("body"in e?Promise.resolve(e.body):e.blob()).then((function(a){return new Response(a,{headers:e.headers,status:e.status,statusText:e.statusText})})):Promise.resolve(e)},createCacheKey=function(e,a,t,n){var c=new URL(e);return n&&c.pathname.match(n)||(c.search+=(c.search?"&":"")+encodeURIComponent(a)+"="+encodeURIComponent(t)),c.toString()},isPathWhitelisted=function(e,a){if(0===e.length)return!0;var t=new URL(a).pathname;return e.some((function(e){return t.match(e)}))},stripIgnoredUrlParameters=function(e,a){var t=new URL(e);return t.hash="",t.search=t.search.slice(1).split("&").map((function(e){return e.split("=")})).filter((function(e){return a.every((function(a){return!a.test(e[0])}))})).map((function(e){return e.join("=")})).join("&"),t.toString()},hashParamName="_sw-precache",urlsToCacheKeys=new Map(precacheConfig.map((function(e){var a=e[0],t=e[1],n=new URL(a,self.location),c=createCacheKey(n,hashParamName,t,!1);return[n.toString(),c]})));function setOfCachedUrls(e){return e.keys().then((function(e){return e.map((function(e){return e.url}))})).then((function(e){return new Set(e)}))}self.addEventListener("install",(function(e){e.waitUntil(caches.open(cacheName).then((function(e){return setOfCachedUrls(e).then((function(a){return Promise.all(Array.from(urlsToCacheKeys.values()).map((function(t){if(!a.has(t)){var n=new Request(t,{credentials:"same-origin"});return fetch(n).then((function(a){if(!a.ok)throw new Error("Request for "+t+" returned a response with status "+a.status);return cleanResponse(a).then((function(a){return e.put(t,a)}))}))}})))}))})).then((function(){return self.skipWaiting()})))})),self.addEventListener("activate",(function(e){var a=new Set(urlsToCacheKeys.values());e.waitUntil(caches.open(cacheName).then((function(e){return e.keys().then((function(t){return Promise.all(t.map((function(t){if(!a.has(t.url))return e.delete(t)})))}))})).then((function(){return self.clients.claim()})))})),self.addEventListener("fetch",(function(e){if("GET"===e.request.method){var a,t=stripIgnoredUrlParameters(e.request.url,ignoreUrlParametersMatching);(a=urlsToCacheKeys.has(t))||(t=addDirectoryIndex(t,"index.html"),a=urlsToCacheKeys.has(t));0,a&&e.respondWith(caches.open(cacheName).then((function(e){return e.match(urlsToCacheKeys.get(t)).then((function(e){if(e)return e;throw Error("The cached response that was expected is missing.")}))})).catch((function(a){return fetch(e.request)})))}}));