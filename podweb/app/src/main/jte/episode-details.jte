@import podweb.models.Episode
@import podweb.models.Podcast
@import podweb.models.User
@import podweb.models.Listen

@param Episode episode
@param boolean withDetails = false
@param boolean logged = false
@param Listen listen = null

<div class="my-3">
    <div class="flex items-center">
        <a href="/episodes/${episode.id}" class="flex-1">
            <p class=" font-bold my-2 text-2xl line-clamp-1 podcast-link-title" title="${episode.title}">${episode.title}</p>
        </a>
        @if(logged && listen != null)
        <div class="flex mx-3 space-x-2 items-center text-lg font-bold">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-10 h-10">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M15.91 11.672a.375.375 0 0 1 0 .656l-5.603 3.113a.375.375 0 0 1-.557-.328V8.887c0-.286.307-.466.557-.327l5.603 3.112Z" />
            </svg>
            <span>${listen.listening_count}</span>
        </div>
        @endif
        <span class="flex items-center mr-2">
            <!-- time icon -->
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-10 h-10 mr-2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
              </svg>
            <span class="font-bold text-lg">${"   " + episode.duration()}</span>
        </span>
        <a href="/episodes/${episode.id}/#play" class="hover:bg-orange-300 rounded-sm p-1">
            <!-- play icon -->
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-10 h-10">
                <path fill-rule="evenodd"
                    d="M4.5 5.653c0-1.427 1.529-2.33 2.779-1.643l11.54 6.347c1.295.712 1.295 2.573 0 3.286L7.28 19.99c-1.25.687-2.779-.217-2.779-1.643V5.653Z"
                    clip-rule="evenodd" />
            </svg>
        </a>
        <a href="/episodes/${episode.id}" class="hover:bg-blue-300 rounded-sm p-1">
            <!-- comments icon -->
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-10 h-10">
                <path fill-rule="evenodd"
                    d="M4.848 2.771A49.144 49.144 0 0 1 12 2.25c2.43 0 4.817.178 7.152.52 1.978.292 3.348 2.024 3.348 3.97v6.02c0 1.946-1.37 3.678-3.348 3.97a48.901 48.901 0 0 1-3.476.383.39.39 0 0 0-.297.17l-2.755 4.133a.75.75 0 0 1-1.248 0l-2.755-4.133a.39.39 0 0 0-.297-.17 48.9 48.9 0 0 1-3.476-.384c-1.978-.29-3.348-2.024-3.348-3.97V6.741c0-1.946 1.37-3.68 3.348-3.97ZM6.75 8.25a.75.75 0 0 1 .75-.75h9a.75.75 0 0 1 0 1.5h-9a.75.75 0 0 1-.75-.75Zm.75 2.25a.75.75 0 0 0 0 1.5H12a.75.75 0 0 0 0-1.5H7.5Z"
                    clip-rule="evenodd" />
            </svg>
        </a>
    </div>

    <div class="text-gray-600 flex-1 block line-clamp-6">$unsafe{episode.description.replace("\\n", "")}</div>
    @if(withDetails)
        <div
            x-data="{
                eid: ${ episode.id },
                play: false,
                progress: 0,
                duration: 0,
                latestProgress: 0,
                logged: ${logged},
                done: false,
                sendProgress: () => {
                    if ($data.logged == false || $data.latestProgress == $data.progress) return;
                    fetch('/episodes/' + $data.eid + '/progression',
                        {
                            method: 'POST',
                            body: 'progression='+ Math.floor($data.progress)
                        }
                    );
                    $data.latestProgress = $data.progress;
                },
                saveProgress(e) {
                    $data.progress = e.target.currentTime;
                    if ($data.progress + 1 > $data.duration) {
                        if ($data.done == false && $data.duration > 0){
                            $data.sendProgress();
                            $data.done = true;
                        }
                    } else {
                        $data.done = false;
                    }
                },
                setup() {
                    setInterval($data.sendProgress, 1000 * 15);
                    $refs.el.currentTime = ${listen != null ? listen.progression : 3} - 3;
                    if ($refs.el.currentTime < 0) $refs.el.currentTime = 0;
                }
            }"
            x-init="setup()"
        >
            <audio src="${episode.audio_url}"
                @play="play = true" 
                @pause="play = false" 
                @timeupdate="saveProgress($event)" 
                preload="metadata"
                @seeked="sendProgress()"
                @durationchange="duration = $event.target.duration"
                controls
                class="w-full my-3 border-blue-600 border rounded-md bg-blue-100" x-ref="el">
            </audio>

        </div>
    @endif
</div>

<style>
    .podcast-link-title {
        cursor: pointer;
        margin-right: 1rem;
    }

    .podcast-link-title:hover {
        color: blue;
    }
</style>
