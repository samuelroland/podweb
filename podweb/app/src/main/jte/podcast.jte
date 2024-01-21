@import podweb.models.Episode
@import podweb.models.Podcast
@import podweb.models.User
@import java.util.ArrayList

@param Podcast podcast
@param Object loggedUser
@param ArrayList<Episode> episodes

    @template.layout(
    content = @`
    @template.podcast-square(podcast, true)

    <h3 class="">Episodes</h3>
    <div class="">
        @for(Episode episode : episodes)
        <div class=" my-3">
            <div class="flex">
                <p class="flex-1 font-bold  my-2 text-2xl line-clamp-1" title="${episode.title}">${episode.title}</p>

                <a href="/queue/${episode.id}/play" class="hover:bg-orange-300 rounded-sm p-1">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-10 h-10">
                        <path fill-rule="evenodd"
                            d="M4.5 5.653c0-1.427 1.529-2.33 2.779-1.643l11.54 6.347c1.295.712 1.295 2.573 0 3.286L7.28 19.99c-1.25.687-2.779-.217-2.779-1.643V5.653Z"
                            clip-rule="evenodd" />
                    </svg>
                </a>
                <a href="/episodes/${episode.id}" class="hover:bg-blue-300 rounded-sm p-1">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-10 h-10">
                        <path fill-rule="evenodd"
                            d="M4.848 2.771A49.144 49.144 0 0 1 12 2.25c2.43 0 4.817.178 7.152.52 1.978.292 3.348 2.024 3.348 3.97v6.02c0 1.946-1.37 3.678-3.348 3.97a48.901 48.901 0 0 1-3.476.383.39.39 0 0 0-.297.17l-2.755 4.133a.75.75 0 0 1-1.248 0l-2.755-4.133a.39.39 0 0 0-.297-.17 48.9 48.9 0 0 1-3.476-.384c-1.978-.29-3.348-2.024-3.348-3.97V6.741c0-1.946 1.37-3.68 3.348-3.97ZM6.75 8.25a.75.75 0 0 1 .75-.75h9a.75.75 0 0 1 0 1.5h-9a.75.75 0 0 1-.75-.75Zm.75 2.25a.75.75 0 0 0 0 1.5H12a.75.75 0 0 0 0-1.5H7.5Z"
                            clip-rule="evenodd" />
                    </svg>
                </a>
            </div>

            <div class="text-gray-600 flex-1 block line-clamp-6">$unsafe{episode.description}</div>

        </div>
        @endfor

        `,
        loggedUser
        )