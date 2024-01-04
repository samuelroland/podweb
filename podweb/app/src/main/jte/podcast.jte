@import podweb.models.Podcast

@param Podcast podcast

<a href="/podcasts/${podcast.id}">
	<div class="mx-2 my-1 w-64 hover:bg-blue-100 p-1">
		<img src="${podcast.image}" alt="" class="w-full">
		<p class="font-bold line-clamp-1 mt-1 mb-2">${podcast.title}</p>
		<div class="flex text-gray-600">
			<p class="flex-1">${podcast.author}</p>
			<p class="">${podcast.episodes_count}</p>
		</div>
	</div>
</a>