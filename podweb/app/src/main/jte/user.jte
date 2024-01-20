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
            <div class="user-info">
                <p><strong><span style="font-size: 1.4em;">User:</span></strong><span style="font-size: 1.4em;"> ${user.firstname} ${user.lastname}</span></p>
                <p><strong><span style="font-size: 1.4em;">Registration Date:</span></strong><span style="font-size: 1.4em;"> DATE</span></p>
                <!-- Replace with the actual registration date format you use -->
            </div>

            <!-- User Information Section -->
            <div class="user-info mb-6">
                <h2 class="text-2xl font-semibold">Badges</h2>
                <!-- Display user badges -->
                <div class="badges">
                    @for(Badge badge : badges)
                        <li class="p-4 bg-white rounded-md shadow-md flex justify-between items-center">
                        <div class="podcast-info flex-grow mx-4">
                            <h2 class="text-lg font-semibold">${badge.name}</h2>
                        </div>
                        </li>
                    @endfor
                </div>
            </div>

            <!-- Playlists Section -->
            <div class="playlists">
                <h2 class="text-2xl font-semibold">Playlists</h2>
                <ul class="space-y-4">
                    <!-- Display user playlists -->
                    @for(Playlist playlist : playlists)
                        <li class="p-4 bg-white rounded-md shadow-md flex justify-between items-center">
                        <div class="podcast-info flex-grow mx-4">
                            <h2 class="text-lg font-semibold">${playlist.name}</h2>
                            <h4 class="text-lg">$unsafe{playlist.description}</h4>
                        </div>
                        </li>
                    @endfor
                </ul>
            </div>
        </div>
    `,
    loggedUser
    )