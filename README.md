# Member Club - Assignment 3 (OOP)

A rental management system for a member club built in Java, demonstrating object-oriented programming principles.

## Table of Contents

- [About the Project](#about-the-project)
- [Functionality](#functionality)
- [Project Structure](#project-structure)
- [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
- [How to Run](#how-to-run)
- [Usage](#usage)
- [Technical Choices](#technical-choices)
- [Statistics](#statistics)
- [Requirements Met](#requirements-met)

## About the Project

Built as part of YH education in Java System Development. The goal was to demonstrate understanding of OOP concepts including inheritance, polymorphism, interfaces, and design patterns.

I chose to build a complete rental system with authentication, shopping cart functionality, and a pricing strategy pattern from the start. Could have made it simpler but wanted to show proper separation of concerns and scalable architecture.

## Functionality

### Core Features

- User authentication (login/logout/register)
- Member management (add/remove/search members)
- Rental transactions with shopping cart
- Return handling with late fee calculation
- Membership tiers with automatic discounts
- Detailed receipts for rentals and returns
- Revenue tracking

### Rental Items

- **Camping equipment:** tents, sleeping bags, backpacks, lanterns, trangia kitchens
- **Water vehicles:** kayaks, motor boats, electric boats, row boats
- **Fishing equipment:** rods, nets, bait

### Membership Levels

- **STANDARD** - no discount
- **STUDENT** - 20% discount
- **PREMIUM** - 30% discount

## Project Structure

```
src/
├── Main.java                                        # Entry point
└── com/memberclub/
    ├── model/                                       # Data classes
    │   ├── User.java                                # System user
    │   ├── Member.java                              # Club member
    │   ├── Rental.java                              # Rental transaction
    │   ├── Item.java                                # Abstract base for all items
    │   ├── camping/                                 # Camping equipment classes
    │   │   ├── CampingEquipment.java                # Abstract base
    │   │   ├── Tent.java
    │   │   ├── SleepingBag.java
    │   │   ├── Backpack.java
    │   │   ├── TrangiaKitchen.java
    │   │   └── Lantern.java
    │   ├── fishing/                                 # Fishing equipment classes
    │   │   ├── FishingEquipment.java                # Abstract base
    │   │   ├── FishingRod.java
    │   │   ├── FishingNet.java
    │   │   └── FishingBait.java
    │   ├── vehicles/                                # Water vehicle classes
    │   │   ├── WaterVehicle.java                    # Abstract base
    │   │   ├── Boat.java                            # Abstract base
    │   │   ├── MotorBoat.java
    │   │   ├── ElectricBoat.java
    │   │   ├── RowBoat.java
    │   │   └── Kayak.java
    │   └── enums/                                   # All enumeration types
    │       ├── MembershipLevel.java
    │       ├── ItemStatus.java
    │       ├── RentalStatus.java
    │       ├── ItemType.java
    │       ├── RentalPeriod.java
    │       ├── Color.java
    │       ├── Material.java
    │       ├── FuelType.java
    │       ├── BackpackType.java
    │       ├── TentType.java
    │       ├── SeasonRating.java
    │       ├── PowerSource.java
    │       ├── RodType.java
    │       ├── NetSize.java
    │       ├── BaitType.java
    │       └── KayakType.java
    ├── service/                                     # Business logic
    │   ├── Inventory.java                           # Item management
    │   ├── MemberRegistry.java                      # Member management
    │   ├── RentalService.java                       # Rental transactions
    │   ├── MembershipService.java                   # Member operations
    │   └── RevenueService.java                      # Financial tracking
    ├── pricing/                                     # Strategy pattern for pricing
    │   ├── PricePolicy.java                         # Interface
    │   ├── PricingFactory.java                      # Factory for strategy selection
    │   ├── StandardPricing.java                     # No discount
    │   ├── StudentPricing.java                      # 20% discount
    │   └── PremiumPricing.java                      # 30% discount
    ├── system/                                      # Core infrastructure
    │   ├── ClubSystem.java                          # Central coordinator
    │   ├── ItemIdGenerator.java                     # Generates item IDs
    │   ├── MemberIdGenerator.java                   # Generates member IDs
    │   └── SampleDataLoader.java                    # Preloads demo data
    ├── ui/                                          # User interface
    │   ├── ConsoleMenu.java                         # Main menu controller
    │   ├── RentalView.java                          # Rental operations
    │   ├── ItemView.java                            # Item display
    │   ├── MemberView.java                          # Member management
    │   ├── UIHelper.java                            # Display utilities
    │   ├── validation/                              # Input validation
    │   │   └── InputValidator.java                  # Input validation utilities
    │   └── components/                              # UI components
    │       ├── CartItem.java                        # Cart item model
    │       ├── ShoppingCart.java                    # Cart before checkout
    │       ├── ItemSelector.java                    # Item selection
    │       ├── MemberSelector.java                  # Member selection
    │       ├── ReceiptGenerator.java                # Receipt formatting
    │       └── ReturnHandler.java                   # Return processing
    └── exceptions/                                  # Custom exceptions
        ├── ItemNotFoundException.java
        ├── ItemNotAvailableException.java
        ├── MemberNotFoundException.java
        └── RentalNotFoundException.java
```

Organized into clear packages where each has a specific responsibility.

## OOP Concepts Demonstrated

### Inheritance Hierarchy

- Abstract `Item` class as base for all rental items
- `CampingEquipment`, `FishingEquipment`, `WaterVehicle` as intermediate abstracts
- Concrete classes like `Tent`, `FishingRod`, `Kayak` inheriting behavior

### Polymorphism

- `Item.getItemType()` returns different types based on concrete class
- Collections of `Item` containing mixed object types
- `PricePolicy` interface with multiple implementations

### Design Patterns

- **Strategy Pattern:** Different pricing strategies (Standard/Student/Premium)
- **Factory Pattern:** `PricingFactory` selects correct pricing strategy
- **MVC Pattern:** Separation of Model, View, and Controller logic
- **Component Pattern:** Reusable UI components

### Encapsulation

- Private fields with public getters/setters
- Validation in service layer
- Business logic separated from UI

## How to Run

### Prerequisites

- Java 17 or higher
- Terminal/Command Prompt or IntelliJ IDEA

### Compile and Run

**From terminal:**


**From terminal (Unix/Mac/Git Bash):**
```bash
# Compile all files
javac -d out src/Main.java src/com/memberclub/**/*.java

# Run
java -cp out Main
```

**From PowerShell (Windows):**
```powershell
# Compile all files
javac -d out -sourcepath src src/Main.java

# Run
java -cp out Main
```

**Using IntelliJ IDEA:**

1. Open project
2. Right-click `Main.java`
3. Select "Run 'Main.main()'"


**Using IntelliJ IDEA:**

1. Open project
2. Right-click `Main.java`
3. Select "Run 'Main.main()'"

## Usage

### Workflow

1. **Start the program** - launches main menu
2. **Register a new user account** - create credentials
3. **Login** with your credentials
4. **Navigate main menu** - access all features
5. **Select member** to rent for
6. **Add items to shopping cart** - browse categories
7. **Choose rental period** - hourly or daily
8. **Checkout** - get receipt with discount applied
9. **Return items** - late fees apply if overdue

### Preloaded Demo Data

- 3 members (Standard, Student, Premium)
- 16 rental items across all categories
- Ready to test immediately after login

### Try These Features

- Login and rent multiple items in one transaction
- Rent as STUDENT member to see 20% discount applied
- Return items late to see late fee calculation
- Register new user (or member)
- Search for members by name
- View member rental history with status

Everything should work without crashes. All input is validated.

## Technical Choices

### Three-Level Inheritance

Base abstract (`Item`) → category abstract (`CampingEquipment`) → concrete class (`Tent`). Shows understanding of when to use abstract classes vs interfaces.

### Strategy Pattern for Pricing

Could have used if-statements everywhere but Strategy pattern makes it easy to add new membership levels without touching existing code. Open-closed principle.

### Shopping Cart System

Cart holds `CartItem` objects before conversion to `Rental`. Separates temporary shopping state from permanent rental records.

### ID Generation

Prefixed IDs (`TENT-001`, `ROD-002`) instead of plain numbers. Makes it obvious what type of item it is when debugging.

### HashMap for Lookups

Used `HashMap<ID, Object>` in registries for O(1) lookup instead of `ArrayList`. Performance matters even in small programs.

### Late Fee Calculation

`daysLate × dailyPrice` - charges the same daily rate for late days.

### Input Validation

`InputValidator` utility prevents crashes from invalid input. Try typing letters when numbers expected - program handles it gracefully.

### Clear Screen

ANSI codes for better UX. Clears between operations so menu doesn't scroll away.

## Statistics

- **67 Java files** across 12 packages
- **20 model classes** (including 3 abstract bases)
- **5 service classes** for business logic
- **5 pricing classes** implementing strategy pattern
- **4 system classes** for core infrastructure
- **12 UI classes** for user interaction
- **16 enumerations** for type safety
- **4 custom exceptions** for error handling
- **Zero external dependencies** - pure Java standard library

## Requirements Met

- Inheritance (3-level hierarchy)
- Polymorphism (abstract methods, interface implementations)
- Encapsulation (private fields, public interfaces)
- Composition (services composed into ClubSystem)
- Design patterns (Strategy)
- Exception handling (custom exceptions, try-catch)
- Collections (HashMap, ArrayList, List)

### Additional Features (Beyond Requirements)

- Factory pattern for pricing strategy selection
- MVC architecture for scalability
- Shopping cart system for better UX
- Enums for type safety
- Input validation utilities

---

**Author:** Daniel Eriksson  
**Course:** Java System Development (YH)  
**Assignment:** OOP Fundamentals (Assignment 3)
