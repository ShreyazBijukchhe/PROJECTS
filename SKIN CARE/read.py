# Constants
MARKUP_PERCENTAGE = 200  # 200% markup
FREE_ITEM_RATIO = 3      # Buy 3 get 1 free policy

def read_inventory(file_path):
    """Read inventory from file and return as list of dictionaries"""
    inventory = []
    try:
        with open(file_path) as file:
            next(file)  # Skip header
            for line in file:
                line = line.strip()
                if not line:
                    continue
                try:
                    parts = [item.strip() for item in line.split(',')]
                    cost_price = int(parts[4])
                    selling_price = cost_price * (1 + MARKUP_PERCENTAGE / 100)
                    
                    inventory.append({
                        "Brand": parts[0],
                        "Product": parts[1],
                        "Country": parts[2],
                        "Stock": int(parts[3]),
                        "Cost Price": cost_price,
                        "Selling Price": selling_price
                    })
                except (IndexError, ValueError) as e:
                    print(f"Error in line: '{line}' → {e}")
    except FileNotFoundError:
        print(f"Error: Inventory file '{file_path}' not found!")
    return inventory

def calculate_free_items(quantity):
    """Calculate free items based on 'buy 3 get 1 free' policy"""
    return quantity // FREE_ITEM_RATIO