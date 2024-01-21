@import podweb.models.User
@import podweb.models.Badge
@import podweb.models.Playlist
@import java.util.ArrayList

@param Object loggedUser
@param User user
@param ArrayList<Badge> badges
@param ArrayList<Playlist> playlists

@template.layout(
    content = @`
        <div class="container mx-auto my-8">
            <h1 class="text-3xl font-bold text-center mb-6">${user.firstname}'s Profile</h1>

            <!-- Display user information -->
            <div class="user-info text-lg mb-6">
                <p><strong>User:</strong> ${user.firstname} ${user.lastname}</p>
                <p><strong>Registration Date:</strong> ${user.registration_date()}</p>
            </div>

            <!-- User Badges Section -->
            <div class="user-badges mb-8">
                <h2 class="text-2xl font-semibold mb-4">Badges</h2>
                <!-- Display user badges -->
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    @for(Badge badge : badges)
                        <div class="p-4 bg-white rounded-md shadow-md">
                            <div class="badge-info">
                                <h2 class="text-lg font-semibold">${badge.name}</h2>
                                <p class="text-gray-600">${badge.description}</p>
                                <p class="text-gray-600">Points: ${badge.points}</p>
                            </div>
                        </div>
                    @endfor
                </div>
            </div>

            <!-- User Playlists Section -->
            <div class="user-playlists">
                <h2 class="text-2xl font-semibold mb-4">Playlists</h2>
                <ul class="space-y-4">
                    <!-- Display user playlists -->
                    @for(Playlist playlist : playlists)
                        <li class="p-4 bg-white rounded-md shadow-md">
                            <div class="playlist-info">
                                <h2 class="text-lg font-semibold">${playlist.name}</h2>
                                <p class="text-gray-600">${playlist.description}</p>
                            </div>
                        </li>
                    @endfor
                </ul>
            </div>
        </div>
    `,
    loggedUser
)
