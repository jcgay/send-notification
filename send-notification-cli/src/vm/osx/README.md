# OS X Box

## Usage

You will need [`vagrant`](https://www.vagrantup.com) to manage the environment.

    vagrant up
    
will start a VM in GUI mode.  

The `target` project directory is accessible from a mount at `/app`.  
Scripts to launch a notification with each notifier are available in `/vagrant/notifiers`.

## Construction

The working box comes from [https://atlas.hashicorp.com/jhcook/boxes/osx-yosemite-10.10](https://atlas.hashicorp.com/jhcook/boxes/osx-yosemite-10.10).

### Share folder between host and guest

`VirtualBox` doesn't have Guest additions for OS X, so we can't have shared folders.  
Instead we can [synced folders using NFS](https://docs.vagrantup.com/v2/synced-folders/nfs.html).

We need to setup a [private network](https://docs.vagrantup.com/v2/networking/private_network.html) to access our guest machine by some address that is not publicly accessible from the global internet.  
I have tried doing so using `dhcp` but if fails with message:

    No guest IP was given to the Vagrant core NFS helper. This is an
    internal error that should be reported as a bug.

It works with static ip, just had to use [`10.0.0.100`](http://leafac.com/bugs/2013/05/21/vagrant-host-only-network-troubleshooting.html) as it was failing with others ip (even if they were not used)

    The specified host network collides with a non-hostonly network!
    This will cause your specified IP to be inaccessible. Please change
    the IP or name of your host only network so that it no longer matches that of
    a bridged or non-hostonly network.
    
### Install Xcode command line tools

Have taken the script from [https://github.com/boxcutter/osx/blob/master/script/xcode-cli-tools.sh](https://github.com/boxcutter/osx/blob/master/script/xcode-cli-tools.sh)
    
### Growl

`growl` is available through the appstore and is not free.