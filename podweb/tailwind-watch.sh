# Hacky command to run the tailwind CLI downloaded by the Gradle plugin "au.id.wale.tailwind" to avoid needing the installation of NPM just to use this.
~/.gradle/caches/au.id.wale.tailwind/3.4.0/tailwindcss-linux-x64 \
	-i ./app/src/main/static/style.css \
	-o ./app/src/main/static/out.css \
	-c ./app/src/main/static/tailwind.config.js\
	--watch=always -v