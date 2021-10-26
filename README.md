# ClansLite
ClansLite is a light-weight clans plugin for Minecraft servers running Spigot!

ClansLite does not support any grief prevention tools such as land claiming or securing containers within your clan.

## /clan command
Aliases: `/clans`, `/c`  
  
The `/clan` command is the main command of the plugin, with `/clan` you can do the following:
* `/clan create <name>` - Creates A new clan if not already in one
* `/clan disband` - If you are the clan owner, this will destroy your clan
* `/clan invite <player>` - Will invite a player to your clan if they are not already in one
* `/clan kick <player>` - Will kick a player from your clan
* `/clan info` - Will display information about your current clan
* `/clan list` - Will list all clans in the server
* `/clan prefix <prefix>` - Will change the prefix for your clan in chat.

## /clanadmin command
Aliases: `/ca`  

The `/clanadmin` command is purely for server admins.Only 1 argument is implemented which is `/clanadmin save` - which will save all current clan info to the `clans.json` data file.

## Permissions
ClansLite comes with 2 permissions:
* clanslite.clan
* clanslite.admin

`clanslite.clan` is by default given to everyone so they can all create, edit and manage a clan.  
`clanslite.admin` is by default given to server operators.

## Config
The max clan size (by default is 8), can be managed in the `plugins/ClansLite/config.yml` file.

## Chat prefix
ClansLite exposes a variable of `{CLAN}` to use in Essentials Chat or similar.

###This plugin is in a Beta stage and caution should be used when using it on production servers, please report any issues in GitHub and feel free to join my [discord](https://discord.gg/ZECTYBw5qr).