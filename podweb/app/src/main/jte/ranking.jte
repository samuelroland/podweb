@import podweb.models.RankedPodcast
@import java.util.ArrayList

@param Object loggedUser
@param ArrayList<RankedPodcast> rankedPodcasts

@template.layout(
content = @`
    <div class="container mx-auto p-8">
        <h1 class="text-4xl font-bold text-center mb-12 text-gray-800">Podcast Ranking</h1>
        <div class="podcast-listing max-w-4xl mx-auto bg-white shadow-lg rounded-lg">
            <ul class="divide-y divide-gray-200">
                @for(RankedPodcast podcast : rankedPodcasts)

                    <li class="podcast-ranking flex items-center py-4 px-6 hover:bg-gray-50 transition-colors">
                        <a href="/podcasts/${podcast.id}"
                           class="flex items-center py-4 px-6 w-full h-full text-current no-underline">
                            <span class="rank text-2xl font-bold text-indigo-600 mr-4">${rankedPodcasts.indexOf(podcast) + 1}.</span>
                            <div class="podcast-image mr-6">
                                <img src="${podcast.image}" alt="${podcast.title}" class="rounded-full">
                            </div>
                            <div class="podcast-info flex-grow">
                                <h4 class="text-xl font-semibold">${podcast.title}</h4>
                                <p class="authors">Authors: <span class="text-gray-600">${podcast.author}</span></p>
                                <p class="listenings">Listenings: <span
                                            class="text-gray-600">${podcast.listenings_total}</span></p>
                            </div>
                        </a>
                    </li>

                @endfor
            </ul>
        </div>
    </div>
`,
loggedUser
)
<style>
    body {
        background-color: #f7fafc;
        font-family: 'Helvetica', 'Arial', sans-serif;
    }

    .container {
        box-sizing: border-box;
    }

    .podcast-listing {
        overflow: hidden;
    }

    .podcast-ranking {
        align-items: center;
        transition: background-color 0.3s ease-in-out;
    }

    .podcast-ranking:hover {
        background-color: #f9fafb;
    }

    .rank {
        width: 30px;
        color: #4f46e5;
    }

    .podcast-image {
        flex-shrink: 0;
        margin-right: 20px;
    }

    .podcast-image img {
        width: 80px;
        height: 80px;
        object-fit: cover;
    }

    .podcast-info h4 {
        color: #1a202c;
        margin: 0 0 4px;
    }

    .podcast-info p {
        margin: 0;
        font-size: 0.875rem;
    }

    .authors span, .listenings span {
        font-weight: 500;
    }

    .hover\:bg-gray-50:hover {
        background-color: #f9fafb;
    }

    .text-indigo-600 {
        color: #4f46e5;
    }

    .text-gray-600 {
        color: #4a5568;
    }

    .divide-y > li:not(:first-child) {
        border-top: 1px solid #e2e8f0;
    }

    .divide-gray-200 > li {
        border-color: #e2e8f0;
    }
</style>
