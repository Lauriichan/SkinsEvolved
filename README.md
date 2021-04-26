# SkinsEvolved ([Spigot](https://www.spigotmc.org/resources/91756/))
  
An easy to use and simple plugin to manipulate player skins 
or restore premium skins on offline-mode servers


## Commands

The command names of SkinsEvolved are:
- skinsevolved
- skinevolved
- skins
- skin

And there are only 4 commands:

<B>/skinsevolved permissions </B>(skinsevolved.permissions)<br>
This will list all permissions that SkinsEvolved has.

<B>/skinsevolved reload </B>(skinsevolved.reload)<br>
This will list all permissions that SkinsEvolved has.<br><br>

The last two commands got the same permissions.<br>
For yourself: skinsevolved.command.self<br>
And for others: skinsevolved.command.other<br>

If you want to use one of the commands below on someone else, you only need to specify the player at the end of the command.<br>
For example: "/skinsevolved update Lauriichan"<br><br>

<B>/skinsevolved update</B><br>
This will update the Skin of the target player to the latest available at Mojang

<B>/skinsevolved use</B><br>
This command has multiple options.<br>
You can download skins from URLs by specifying "url <URL> <Model>"<br>
The model is for example alex (3px arms) or steve (4px arms)<br>
For the URL download to work you need to specify at least one Minecraft profile in the <i>mojang.json</i> that is located at <i>plugins/SkinsEvolved</i>.<br>
An example on how to specify it can be found in the file itself<br>
Then there is the option to download a already uploaded skin from a player.<br>
To do that you can either specify the unique id with "uuid <UniqueId>" or<br>
You can use the players name with "name <PlayerName>"<br>

## Permissions

Most of the permissions are already explained above so I will only explain not yet listed permissions here.

<B>skinsevolved.*</B><br>
This is the Wildcard permissions of SkinsEvolved.<br>
Operators got it by default and it grants all other permissions.
- skinsevolved.permanent
- skinsevolved.command.*
- skinsevolved.permissions
- skinsevoled.reload

<B>skinsevolved.command.*</B><br>
This is again a Wildcard permission of SkinsEvolved.<br>
This time it only grants the two command permissions of it.
- skinsevolved.command.self
- skinsevolved.command.other

<B>skinsevoled.permanent</B><br>
This permission is to restore the skin of the player on join.<br>
For example, if a skin was set while the player is on the server, they will lose it after rejoining.<br>
By granting this permission the skin will be restored on join.<br>
Also if no custom skin is set yet, then it will restore the real skin of the player.<br>
This means that people who join on cracked servers will receive the skin of the premium account that got the same name as them if no custom skin is set.


## That's it!

I hope you will enjoy the plugin<br>
If there are any issues don't hesitate to report them to me on [GitHub](https://github.com/Lauriichan/SkinsEvolved/issues).

Also just as a side note for developers, this project was created using [vCompat](https://github.com/SourceWriters/vCompat).<br>
A lib I'm working on to allow cross-compatibility between multiple versions.<br>
Currently (26.04.2021), it supports 1.8 - 1.16.5 and helps with things like Nbt data of items, entity manipulation using packets and a lot of other things
