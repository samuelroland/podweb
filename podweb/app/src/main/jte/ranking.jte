@import podweb.models.Podcast
@import java.util.ArrayList

@param Object loggedUser
@param ArrayList<Podcast> rankedPodcasts

    @template.layout(
    content = @`
    <div class="container mx-auto my-8">
        <h1 class="text-3xl font-bold text-center mb-6">Podcast Ranking</h1>
        
        <ul class="space-y-4">
            @for(Podcast podcast : rankedPodcasts)
                <li class="p-4 bg-white rounded-md shadow-md flex justify-between items-center">
                    <span class="text-xl font-bold">${rankedPodcasts.indexOf(podcast) + 1}.</span>
                    <div class="podcast-info flex-grow mx-4">
                        <h2 class="text-lg font-semibold">${podcast.title}</h2>
                        <p class="mt-5 text-sm text-gray-600">$unsafe{podcast.description}</p>
                    </div>
                </li>
            @endfor
        </ul>
    </div>
    `,
    loggedUser
    )