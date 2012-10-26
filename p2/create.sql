CREATE TABLE Item (
  itemID INT PRIMARY KEY,
  name VARCHAR(100),
  currently DECIMAL(8,2),
  buy_price DECIMAL(8,2),
  first_bid DECIMAL(8,2),
  num_of_bids INT,
  started TIMESTAMP,
  ends TIMESTAMP,
  sellerID VARCHAR(40) NOT NULL,
  description VARCHAR(4000)
  -- FOREIGN KEY (sellerID) REFERENCES User(userID)
);
CREATE TABLE Category (
  itemID INT NOT NULL,
  category VARCHAR(40) NOT NULL,
  PRIMARY KEY (itemID, category)
  -- FOREIGN KEY (itemID) REFERENCES Item(itemID)
);
CREATE TABLE Bids (
  itemID INT NOT NULL,
  userID VARCHAR(40) NOT NULL,
  time TIMESTAMP NOT NULL,
  amount DECIMAL(8,2) NOT NULL
  -- FOREIGN KEY (itemID) REFERENCES Item(itemID),
  -- FOREIGN KEY (userID) REFERENCES User(userID)
);
CREATE TABLE User (
  userID VARCHAR(40) PRIMARY KEY,
  rating INT,
  location VARCHAR(40),
  country VARCHAR(40)
);