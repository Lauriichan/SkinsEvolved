[CENTER][B][SIZE=6]SkinsEvolved
[/SIZE][/B]
[SIZE=4]An easy to use and simple plugin to manipulate player skins 
or restore premium skins on cracked servers
[/SIZE]
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[/CENTER]

[CENTER][SIZE=5][B]Commands[/B]
[/SIZE][/CENTER]
[SIZE=4]The command names of SkinsEvolved are:[/SIZE]
[LIST]
[*][SIZE=4][SIZE=4]skinsevolved[/SIZE][/SIZE]
[*][SIZE=4][SIZE=4]skinevolved[/SIZE][/SIZE]
[*][SIZE=4][SIZE=4]skins[/SIZE][/SIZE]
[*][SIZE=4]skin[/SIZE]
[/LIST]
[SIZE=4]And there are only 4 commands:

[B]/skinsevolved permissions [/B](skinsevolved.permissions)
This will list all permissions that SkinsEvolved has.

[SIZE=4][B]/skinsevolved reload [/B](skinsevolved.reload)
This will list all permissions that SkinsEvolved has.


The last two commands got the same permissions.
For yourself: skinsevolved.command.self
And for others: skinsevolved.command.other

If you want to use one of the commands below on someone else, you only need to specify the player at the end of the command.
For example: "/skinsevolved update Lauriichan"
[/SIZE]

[SIZE=4][B]/skinsevolved update[/B]
This will update the Skin of the target player to the latest available at Mojang[/SIZE]

[SIZE=4][B]/skinsevolved permissions [/B](skinsevolved.permissions)[/SIZE]
This command has multiple options.
[SIZE=4]You can download skins from URLs by specifying "url <URL> <Model>"[/SIZE]
The model is for example alex (3px arms) or steve (4px arms)
Then there is the option to download a already uploaded skin from a player.
[SIZE=4]To do that you can either specify the unique id with "uuid <UniqueId>" or[/SIZE]
You can use the players name with "name <PlayerName>"[/SIZE]
[CENTER][SIZE=4]
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[/SIZE]

[B][SIZE=5]Permissions
[/SIZE][/B][/CENTER]
[SIZE=4]Most of the permissions are already explained above so I will only explain not yet listed permissions here.[/SIZE]

[B]skinsevolved.*[/B]
This is the Wildcard permissions of SkinsEvolved.
Operators got it by default and it grants all other permissions.
[LIST]
[*]skinsevolved.permanent
[*]skinsevolved.command.*
[*]skinsevolved.permissions
[*]skinsevoled.reload
[/LIST]
[B]skinsevolved.command.*[/B]
This is again a Wildcard permission of SkinsEvolved.
This time it only grants the two command permissions of it.
[LIST]
[*]skinsevolved.command.self
[*]skinsevolved.command.other
[/LIST]
[B]skinsevoled.permanent[/B]
This permission is to restore the skin of the player on join.
For example, if a skin was set while the player is on the server, they will lose it after rejoining.
By granting this permission the skin will be restored on join.
Also if no custom skin is set yet, then it will restore the real skin of the player.
This means that people who join on cracked servers will receive the skin of the premium account that got the same name as them if no custom skin is set.

[CENTER]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[B]That's it!
[/B][/CENTER]
[SIZE=4]I hope you will enjoy the plugin
If there are any issues don't hesitate to report them to me on [URL='https://github.com/Lauriichan/SkinsEvolved/issues']GitHub[/URL][/SIZE]

Also just as a side note for developers, this project was created using [URL='https://github.com/SourceWriters/vCompat']vCompat[/URL].
A lib I'm working on to allow cross-compatibility between multiple versions.
Currently (26.04.2021), it supports 1.8 - 1.16.5 and helps with things like Nbt data of items, entity manipulation using packets and a lot of other things
