# Check if the package is already installed; if not, install it
if (!requireNamespace("Kendall", quietly = TRUE)) {
  install.packages("Kendall")
}

# Load the package after installation
library(Kendall)

# Function to calculate Kendall's tau-c
calculate_kendalls_tau_c_from_csv <- function(file_path, x_col, y_col) {
  # Load the CSV file with semicolon as the delimiter
  if (!file.exists(file_path)) {
    stop("File not found. Please check the file path.")
  }
  data <- read.csv(file_path, sep = ";")

  # Convert column names to lowercase for case-insensitive comparison
  col_names_lower <- tolower(names(data))
  x_col_lower <- tolower(x_col)
  y_col_lower <- tolower(y_col)

  # Check available column names
  cat("Available column names in the dataset:", col_names_lower, "\n")

  # Validate column names
  if (!(x_col_lower %in% col_names_lower) || !(y_col_lower %in% col_names_lower)) {
    stop("The specified columns are not present in the data.")
  }

  # Extract the two columns for Kendall's tau-c calculation
  x <- data[[match(x_col_lower, col_names_lower)]]
  y <- data[[match(y_col_lower, col_names_lower)]]

  # Calculate the number of concordant and discordant pairs
  n <- length(x)
  concordant <- 0
  discordant <- 0
  ties <- 0

  for (i in 1:(n - 1)) {
    for (j in (i + 1):n) {
      if (((x[i] > x[j]) && (y[i] > y[j])) || ((x[i] < x[j]) && (y[i] < y[j]))) {
        concordant <- concordant + 1
      } else if (((x[i] > x[j]) && (y[i] < y[j])) || ((x[i] < x[j]) && (y[i] > y[j]))) {
        discordant <- discordant + 1
      } else {
        ties <- ties + 1
      }
    }
  }

  # Calculate Kendall's tau-c using the cor() function from the Kendall package with method = "kendall"
  tau_c <- cor(x, y, method = "kendall", use = "pairwise.complete.obs")

  # Print the results
  cat("Number of concordant pairs:", concordant, "\n")
  cat("Number of discordant pairs:", discordant, "\n")
  cat("Number of tied pairs:", ties, "\n")
  cat("Kendall's tau-c:", tau_c, "\n")

  # Return the tau-c value
  return(tau_c)
}

# Example usage
file_path <- "/Users/petroskarampas/Desktop/CorrelationSpeedup/src/main/resources/TauAData.csv"
x_column_name <- "y" # Replace with the actual name of the x column
y_column_name <- "z" # Replace with the actual name of the y column

kendalls_tau_c <- calculate_kendalls_tau_c_from_csv(file_path, x_column_name, y_column_name)
