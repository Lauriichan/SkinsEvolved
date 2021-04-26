# SkinsEvolved ([Spigot](https://www.spigotmc.org/resources/91756/))
  
An easy to use and simple plugin to manipulate player skins 
or restore premium skins on cracked servers


## Commands

The command names of SkinsEvolved are:
- skinsevolved
- skinevolved
- skins
- skin

And there are only 4 commands:

<B>/skinsevolved permissions </B>(skinsevolved.permissions)
This will list all permissions that SkinsEvolved has.

<B>/skinsevolved reload </B>(skinsevolved.reload)
This will list all permissions that SkinsEvolved has.


The last two commands got the same permissions.
For yourself: skinsevolved.command.self
And for others: skinsevolved.command.other

If you want to use one of the commands below on someone else, you only need to specify the player at the end of the command.
For example: "/skinsevolved update Lauriichan"

<B>/skinsevolved update</B>
This will update the Skin of the target player to the latest available at Mojang

<B>/skinsevolved use</B>
This command has multiple options.
You can download skins from URLs by specifying "url <URL> <Model>"
The model is for example alex (3px arms) or steve (4px arms)
For the URL download to work you need to specify at least one Minecraft profile in the <i>mojang.json</i> that is located at <i>plugins/SkinsEvolved</i>.
An example on how to specify it can be found in the file itself
Then there is the option to download a already uploaded skin from a player.
To do that you can either specify the unique id with "uuid <UniqueId>" or
You can use the players name with "name <PlayerName>"


## Permissions

Most of the permissions are already explained above so I will only explain not yet listed permissions here.

<B>skinsevolved.*</B>
This is the Wildcard permissions of SkinsEvolved.
Operators got it by default and it grants all other permissions.
- skinsevolved.permanent
- skinsevolved.command.*
- skinsevolved.permissions
- skinsevoled.reload

<B>skinsevolved.command.*</B>
This is again a Wildcard permission of SkinsEvolved.
This time it only grants the two command permissions of it.
- skinsevolved.command.self
- skinsevolved.command.other

<B>skinsevoled.permanent</B>
This permission is to restore the skin of the player on join.
For example, if a skin was set while the player is on the server, they will lose it after rejoining.
By granting this permission the skin will be restored on join.
Also if no custom skin is set yet, then it will restore the real skin of the player.
This means that people who join on cracked servers will receive the skin of the premium account that got the same name as them if no custom skin is set.


## That's it!

I hope you will enjoy the plugin
If there are any issues don't hesitate to report them to me on [GitHub](https://github.com/Lauriichan/SkinsEvolved/issues).

Also just as a side note for developers, this project was created using [vCompat](https://github.com/SourceWriters/vCompat).
A lib I'm working on to allow cross-compatibility between multiple versions.
Currently (26.04.2021), it supports 1.8 - 1.16.5 and helps with things like Nbt data of items, entity manipulation using packets and a lot of other things
