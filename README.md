# ClansLite
ClansLite is a light-weight clans plugin for Minecraft servers running Spigot and most of its forks!

ClansLite does not support any grief prevention tools such as land claiming or securing containers within your clan.

ClansLite DOES however offer the ability to disable friendly fire within your clan!

## /clan command
Aliases: `/clans`, `/c`, `cl`  
  
The `/clan` command is the main command of the plugin, with `/clan` you can do the following:
* `/clan create <name>` - Creates A new clan if not already in one
* `/clan disband` - If you are the clan owner, this will destroy your clan
* `/clan leave` - If you are in a clan, this will remove you from it
* `/clan invite <player>` - Will invite a player to your clan if they are not already in one
* `/clan kick <player>` - Will kick a player from your clan
* `/clan info` - Will display information about your current clan
* `/clan list` - Will list all clans in the server
* `/clan prefix <prefix>` - Will change the prefix for your clan in chat
* `/clan ally [add|remove] <ally-owner>` - Will either add or remove an allied clan to yours
* `/clan enemy [add|remove] <ally-owner>` - Will either add or remove an enemy clan to yours
* `/clan pvp` - Will toggle the friendly fire status for your clan
* `/clan [sethome|home]` - Will set a clan home location or teleport you or you clan members to this location.

## /clanadmin command
Aliases: `/ca`, `cla`

The `/clanadmin` command is purely for server admins only. 

4 arguments are implemented which are: 
* `/clanadmin save` - which will save all current clan info to the `clans.yml` data file.  
* `/clanadmin reload` - This reloads the plugins `config.yml` & the `messages.yml` files from disk.
* `/clanadmin disband <owner-name>` - This allows admins to delete any unauthorised clans.
* `/clanadmin about` - This give you an overview of the plugin's core information.

## /cc command
Aliases: /clanchat, /clanc, /cchat, /chat

The `/cc` command is for the sole purpose of utilising the per clan chat. The following syntax is accepted:

`/cc <message>` - This will send a message to only the members of YOUR clan or the clan you are in.

## Permissions
ClansLite comes with `14` permissions:
* `clanslite.*`
* `clanslite.clan`
* `clanslite.admin`
* `clanslite.update`
* `clanslite.bypass`
* `clanslite.bypass.*`
* `clanslite.bypass.homecooldown`
* `clanslite.bypass.chatcooldown`
* `clanslite.bypass.pvp`
* `clanslite.maxclansize.group1`
* `clanslite.maxclansize.group2`
* `clanslite.maxclansize.group3`
* `clanslite.maxclansize.group4`
* `clanslite.maxclansize.group5`
* `clanslite.maxclansize.group6`

`clanslite.*` is a permission to allow access to ALL functions regardless of operator status.

`clanslite.clan` is by default given to everyone so they can all create, edit and manage a clan.  

`clanslite.admin` is by default given to server operators.

`clanslite.update` is the permission node to allow a player to see in game notifications if there is a plugin update available.

`clanslite.bypass` is the permission node to allow a player to bypass all protections and cooldowns.

`clanslite.bypass.*` is the permission node to allow a player to bypass all protections and cooldowns.

`clanslite.bypass.homecooldown` is the permission node to allow a player to bypass the home command cooldown.

`clanslite.bypass.chatcooldown` - is the permission node to allow a player to bypass the clan chat command cooldown.

`clanslite.bypass.pvp` is the permission node to allow a player to bypass the friendly fire protections.

`clanslite.maxclansize.group1` is the permission node to allow only group 1 size of clan.

`clanslite.maxclansize.group2` is the permission node to allow only group 2 size of clan.

`clanslite.maxclansize.group3` is the permission node to allow only group 3 size of clan.

`clanslite.maxclansize.group4` is the permission node to allow only group 4 size of clan.

`clanslite.maxclansize.group5` is the permission node to allow only group 5 size of clan.

`clanslite.maxclansize.group6` is the permission node to allow only group 6 size of clan.

## Config
The max clan size (by default is 8), can be managed in the `plugins/ClansLite/config.yml` file.

The max clan allies (by default is 4), can be managed in the `plugins/ClansLite/config.yml` file.

The max clan enemies (by default is 2), can be managed in the `plugins/ClansLite/config.yml` file.

## Chat prefix
ClansLite exposes a variable of `{CLAN}` to use in Essentials Chat or similar.

## PlaceholderAPI
ClansLite exposes `8` external placeholders using `PlaceholderAPI` to enable the fetching of a players clan name or the clan prefix or if the clan has friendly fire enabled or if the clan has a home set.

The four available placeholders are:
* `%clansLite_clanName%`
* `%clansLite_clanPrefix%`
* `%clansLite_friendlyFire%`
* `%clansLite_clanHomeSet%`
* `%clansLite_clanMembersSize%`
* `%clansLite_clanAllySize%`
* `%clansLite_clanEnemySize%`
* `%clansLite_playerPointBalance%`

To be able to use these The latest release of [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) MUST be installed!  Without it, only the above `{CLAN}` will be available.

###Please report any issues in GitHub and feel free to join my [discord](https://discord.gg/crapticraft).

###Thank you for using my plugin!
