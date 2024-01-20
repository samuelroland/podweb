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
                    <a href="/podcasts/${podcast.id}"> <!-- Replace "/your-target-page-url" with the actual URL -->
                        <li class="p-4 bg-white rounded-md shadow-md flex items-center space-x-4 cursor-pointer">
                            <span class="text-4xl font-bold text-gray-500">${rankedPodcasts.indexOf(podcast) + 1}.</span>

                            <div class="podcast-info flex-grow">
                                <h4 class="text-lg font-semibold">${podcast.title}</h4>
                                <p class="text-gray-600">Authors: ${podcast.author}</p>
                                <p class="text-gray-600">Number of listenings: ${podcast.listenings_total}</p>
                            </div>

                            <!-- Podcast Image (adjust to the height of the box) -->
                            <div class="w-16 h-16 ml-4">
                                <img src="${podcast.image}" alt="Podcast Image" class="object-cover w-full h-full rounded-md">
                            </div>
                        </li>
                    </a>
                @endfor
            </ul>
        </div>
    `,
    loggedUser
)
