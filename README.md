# Market Simulator
### Structure of project
The project uses agents that buy and sell stocks to one another in order to simulate a functional stock exchange. 
Elements of the stock exchange can be changed to study the effects of certain factors on the behaviour of the market as a whole.

The code uses four main classes:
- Agent class
- Stock class
- Company class
- MarketPlace class

### Agent class
The Agent class is an object modelled after stock traders in the stock exchange. An object of type Agent contains all the information 
a stock trader would require to exist and complete operations in the marketplace, albeit simplified. In reality, stock traders don't 
directly buy or sell stocks from one another but through the employ of a stock broker who handles the transaction in their stead.
The information that our simplified model holds is:
- an amount of cash
- a list or portfolio of stocks and the number of each stock they hold
- variables used to weigh the indices of each stock

An agent object has the ability to perform certain operations within the confines of the marketplace.
Such operations allow it to gather information about the stocks, the companies who own the stocks as well as other agents. This information
allow an agent to change their trading tactics in response to their environment, in order to maximise their own assets.

## Stock class
The Stock class is an object modelled after actual stocks published by companies in stock markets. While simplified for practical purposes,
these objects represent common stock which exist in the hands of stock traders and are sold and bought in a stock exchange. In reality there are 
many different types of securities passed around in a marketplace, however this project focuses on equity securities, the simplest of which is 
the common stock. The information that our simplified model of a stock holds is:
- a name
- a company name
- a price
- a pricechange value
- a ticksize
- a list of the past values for stock price

The price change value represents the difference between the previous price of the stock and the current price. The tick size represents 
the smallest amount of change a stock price can have, it can be either negative or positive. 

A stock object has the ability to change its own information, especially its stock price. The stock is responsible for calculating
a new price for itself, as well as returning its name, company's name, current price and price change. The stock class is also responsible 
for the setting of a new tick size through the setTickSize method.

The calculations for Volatility and Price-Earnings ratio also take place inside the Stock class as they are calculated entirely through 
information private to Stock.java.

## Company class
The Company class is an object modelled after companies in the stock exchange. A company object does not technically interact with any agent in the
marketplace, but merely holds certain variables necessary to determining indices related to the company's stock.
These variables include:
- the market capitalisation value
- the net revenue of the company
- the volume of stocks in the marketplace
- the income of the company
- the earnings of the company per stock

Some of the above values, for example the volume of stock, do not change through the simulation as it would raise the complexity of the
simulation to unfeasible levels. Therefore, company income, stock volume and net revenue are constant throughout the simulation.

Agents are also initialised in the marketplace class. A random amount of cash and a portfolio containing a random number of each 
of the ten stocks are given to them.

A company object only makes two calculations, the calculation of the market capitalisation value and the calculation of the Earnings per Stock
index.

## Marketplace class
The MarketPlace class serves as the environment in which the Agents, Stocks, Companies and their interactions exist and operate.
Its main purpose is to initialise instances of each of the main class objects and through performing updating operations, 
enables for the definition of a time dimension.

In the marketplace, ten predetermined Companies and ten predetermined Stocks published by said companies are initialised.

The only operations the marketplace can do is update the stock prices and enable the transactions between the agents.

## Getting Started (Software Based)

### Prerequisites
The final product will be in the form of an executable file which requires an appropriate runtime environment:
* [Java SE RunTime Environment 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html) 

### Installing
To compile the code on your own, simply download the MarketSimulator folder and from outside it use the following commands in a terminal: 

```
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