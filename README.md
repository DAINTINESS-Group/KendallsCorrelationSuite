<p align="center"> 
    <img height=170 src="https://github.com/DAINTINESS-Group/KendallsCorrelationSuite/blob/main/logo.png"/> 
</p>

# <div align="center">Kendall's Correlation Suite</div>

### <div align="center">A suite of algorithms in Java to compute Kendall's tau coefficient.</div>

Kendall's tau correlation coefficient is a non-parametric statistic used to measure the strength and direction of the association between two variables. It computes the [ordinal association](https://en.wikipedia.org/wiki/Ordinal_association "Ordinal association") between two measured variables. A tau value of 1 indicates a perfect positive correlation, where the ranks of both variables increase together, while a value of -1 indicates a perfect negative correlation, where the rank of one variable increases as the rank of the other decreases. A value of 0 suggests no association between the variables.

## Introduction

A new approach in computing Kendall's tau is introduced in this project utilizing the metadata of a raster of a two dimensional array of Tiles and Apache Spark's computational advantages. The algorithm accepts a CSV or TSV dataset as input as well as two selected columns to compute their correlation. By distributing the data to a raster of cells similar to a Cartesian coordinates system, the algorithm drastically decreases the number of comparisons that need to be executed in order to compute the final result. Along with this new approach, in this project a collection of other existing algorithms are implemented that were used to compare their efficiency against this new Tiles approach.

## Setup

#### Install an IDE of your choice

- If you choose either [**Intellij IDEA**](https://www.jetbrains.com/idea/download/#section=windows) (Community or Pro) or [**Eclipse**](https://www.eclipse.org/downloads/)
- Import the project as a Maven project.
- Make sure that JAVA_HOME is pointing to the correct location of your Java 8 installation.

#### Clone the project

Clone the project on your local machine with the following command,

```
git clone git@github.com:DAINTINESS-Group/KendallsCorrelationSuite.git
```

#### Maven

[**Maven**](https://maven.apache.org/) is a powerful build automation tool used primarily for Java projects. It simplifies the build process like compiling code, packaging binaries, and managing dependencies with its project object model (POM) files. Maven uses a central repository to store a wide array of libraries and plugins, which can be easily included in your project, ensuring consistency and saving time.

To enhance consistency and ease of setup for all contributors, this project uses the Maven Wrapper. The Maven Wrapper ensures that everyone uses the same Maven version, regardless of what is installed locally. This avoids "It works on my machine" problems by standardizing the build environment across machines.

## 🛠️ Build with Maven

Inside the root folder of the project, run the following command,

```
./mvnw clean install
```

This will compile, test, and install all required dependencies, ensuring that the project can run on your local machine.

## Testing

To run the project's tests run the following command,

```
./mvnw test
```

## Usage

### Tiles based algorithm

In order to execute the Tiles algorithm you need to provide the algorithm with the following inside the `Client` class

- The `Path` to the `CSV` or `TSV` dataset.
- The first `column1` that contains numeric values.
- The second `column2` that also contains numeric values.

```java
String filePath = "Path/to/your/dataset";
String column1 = "Name of the 1st column";
String column2 = "Name of the 2nd column";
```

Load it into Spark with the following,

```java
SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();
sparkBasedKendallManager.loadDataset(filePath, column1, column2);
```

And finally compute the correlation,

```java
double kendall = sparkBasedKendallManager.calculateKendallTau(column1, column2);
```

### List based algorithms

Create a `Reader` object to read the Dataset

```java
Reader reader = new Reader();
```

Create again the above three String variables plus a 4th one for the delimiter of the dataset.

```java
String filePath = "Path/to/your/dataset";
String column1 = "Name of the 1st column";
String column2 = "Name of the 2nd column";
String delimiter = "Your dataset's delimiter i.e. ',' "
```

Create a `ColumnPair` object

```java
ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
```

Calculate Kendall's tau using the desired method ( `Apache kendall` / `BruteForce` / `Brophy` )

```java
ListBasedKendallMethodsService methods = new ListBasedKendallMethodsService();
IListBasedKendallCalculator bruteForce= methods.getMethod("BruteForce");
double result = BruteForce.calculateKendall(columnPair);
```

## Contributors

[**Petros Karampas**](https://github.com/PetrosKarampas) <br>
[**Panos Vassiliadis**](https://github.com/pvassil)
