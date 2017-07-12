# Klusterfuck developer guide
Note: this is incomplete at the moment, and will slowly get more details. If there's something specific you want 
added, open an issue and I'll prioritize it.

## API docs
Auto-generated API docs are here: [API Docs](client/README.md)

## Setting up your dev environment with minikube
This has been tested with minikube v0.19.0 and v0.19.1. Other versions should most likely
work too.

To install minikube, see this guide: https://kubernetes.io/docs/getting-started-guides/minikube/

### Bulding the klusterfuck images in minikube VM
Pre-requisites:
To build the images, you must have a Java JDK and maven installed and on your path.

To build the klusterfuck images:
1. Open a shell in project root (parent folder of the folder containing this file in your source)
2. Run `minikube docker-env` and follow the instructions (usually this requires running `eval $(minikube docker-env)`)
3. Run `mvn clean install docker:build` (warning: the build for agent-static image will take a long time when running the first time)

All required images should now be available in the minikube VM.

### Setting up libnss-resolver and dnsmasq on OSX
For setup and overall idea for this setup, see: https://passingcuriosity.com/2013/dnsmasq-dev-osx/

Once you have dnsmasq and libnss resolver working properly, set dnsmasq to resolve *.kube.local to minikube
VM's IP (get the IP by running `minikube ip`). For example, the line looks like this in my setup:
`address=/kube.local/192.168.99.100`

## Setting up your dev environment with Linux and kubeadm
TODO:

(BTW, if you are comfortable with Linux and kubeadm, do you really need a environment setup guide? :P)

