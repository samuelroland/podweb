<div>
    <a href="/"><button>Podcasts</button></a>
    <a href="/ranking"><button>Ranking</button></a>
    <a href="/search"><button>Search</button></a>
    <div class="search-box">
        <input type="text" id="search-input" placeholder="Search...">
        <button id="search-btn">Search</button>
    </div>

    <script>
        const searchInput = document.getElementById('search-input');
        const searchBtn = document.getElementById('search-btn');
        const url = '/search?q=';

        searchBtn.addEventListener('click', () => {
            const searchQuery = searchInput.value.trim();
            if (searchQuery !== '') {
                window.location.href = url + encodeURIComponent(searchQuery);
            }
        });
    </script>
</div>