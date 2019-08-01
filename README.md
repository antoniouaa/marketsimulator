# Market Simulator
## Note
This project was previously hosted on GitLab as the individual capstone challenge for University of Essex's CE301 module. The course module required students to create issues on Jira.
I have no idea how to import those issues, or if it's even possible. This repo was created on July 30, 2019, while the original GitLab repo was created in September 2018.
I'm very sorry for any confusion caused, the University's silly policy is to blame.

### Structure of project
The project uses agents that buy and sell stocks to one another in order to simulate a functional stock exchange. 
Elements of the stock exchange can be changed to study the effects of certain factors on the behaviour of the market as a whole.

The code uses four main classes:
- Agent class
- Stock class
- Company class
- MarketPlace class

## Getting Started (Software Based)

### Prerequisites
The final product will be in the form of an executable file which requires an appropriate runtime environment:
* [Java SE RunTime Environment 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

The graphing library JFreeChart is a requisite for the project. You can download it 

### Installing
To compile the code on your own, simply clone the MarketSimulator folder and use the following commands in a terminal: 

```
git clone https://github.com/antoniouaa/marketsimulator
cd marketsimulator

javac MarketSimulator/*.java
java MarketSimulator.Main
```

### Using the application
When the application is first run, the user will be faced with a frame containing settings for the simulation. 
Default settings are preselected, however the user is free to customise these settings.
#### Customising Simulation settings
There are 3 main settings the user can change to their liking.
These settings are: 
- the number of agents
- the number of stocks
- the number of days the simulation runs

![Simulation Settings](images/simulationsettings.PNG)

There exists an additional setting for the user to customise. This setting is brought up through clicking the _Advanced Settings_ button. A new frame 
is brought up with sliders for the Overcommitment setting.
Overcommitment is the weight that controls the amount of stock an agent may purchase in a transaction.

![Overcommitment Setting](images/overcommitmentsettings.PNG)

A sample output of a simulation with 10 agents trading 10 stocks for 50 days would look like this:
![Sample simulation output](images/mainGUI_5.PNG)

#### Menu bar
![Menu bar](images/filemenu_zoomed.png)

An added feature of the program is the menu bar, holding various functionality available to the user.
##### File
In the file submenu, the user is free to start a new simulation, export the graphs to JPEG, export the tables to CSV files or close the application.
##### Help
In the help submenu, the user can find a link to this repository where they may look for information or raise an issue.

### Versioning Strategy
This project will be employing [semantic versioning](https://semver.org/).

## Getting Started (Hardware Based)

### Pre-requisites
No hardware pre-requisites exist.

### References
[Matthew Palmer's TODO markdown language](https://github.com/matthewpalmer/.todo)

[Font used in the application](https://fontmeme.com/fonts/sulphur-point-font/)

## Authors
* Alexandros Antoniou
