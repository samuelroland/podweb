@import podweb.models.RankedPodcast
@import java.util.ArrayList

@param Object loggedUser
@param ArrayList<RankedPodcast> rankedPodcasts

    @template.layout(
    content = @`
        <div class="container mx-auto my-8">
            <h1 class="text-3xl font-bold text-center mb-6">Podcast Ranking</h1>
            
            <ul class="space-y-4">
                @for(RankedPodcast podcast : rankedPodcasts)
                    <li class="p-4 bg-white rounded-md shadow-md flex justify-between items-center">
                        <span class="text-xl font-bold">${rankedPodcasts.indexOf(podcast) + 1}.</span>
                        <div class="podcast-info flex-grow mx-4">
                            <h4 class="text-lg font-semibold">Title: ${podcast.title}</h4>
                            <h4 class="text-lg font-semibold">Authors: ${podcast.author}</h4>
                            <h4 class="text-lg">Number of listenings: ${podcast.listenings_total}</h4>
                        </div>
                    </li>
                @endfor
            </ul>
        </div>
    `,
    loggedUser
    )