# Windows Box

## Usage

You will need [`vagrant`](https://www.vagrantup.com) to manage the environment.

    vagrant up
    
will start a VM in GUI mode.  

The `target` project directory is accessible from a mount at `C:\app`.  
Scripts to launch a notification with each notifier are available in `C:\vagrant`.

## Construction

The base box is a Windows 2012 Server edition because Windows 8.x client does not have WinRM activated by default and I don't find one correctly packaged (using ssh or WinRM) on [vagrantcloud](https://vagrantcloud.com/boxes) or [vagrantbox.es](http://www.vagrantbox.es).  

Chocolatey bootstrap script comes from: [Provisioning a windows box with vagrant and chocolatey](http://www.tzehon.com/2014/01/20/provisioning-a-windows-box-with-vagrant-chocolatey-and-puppet-part-1/).  

Windows notifiers implemented in `send-notification` are then automatically installed.

### Growl

The chocolatey `Growl` package was failing at installation. Resolving `.NET Framework v2` dependency by hand solve this.

### Snarl

Installing `Snarl` in silent mode always throw an `%ERRORLEVEL% == 1223`. There is a hacky workaround to ignore it...

Launching Snarl after a silent install fails with error: 

    snarl requires exec.library v46 or greater
      
This is a [known problem](https://groups.google.com/forum/#!topic/snarl-discuss/db0fjiC-apo), the solution is to install [`melon`](http://sourceforge.net/projects/snarlwin/files/Goodies/setup-minimal.exe/download) before trying to launch `Snarl`.
