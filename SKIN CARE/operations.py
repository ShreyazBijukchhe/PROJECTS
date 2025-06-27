from write import update_inventory_file
from read import calculate_free_items
import datetime

# Constants
MARKUP_PERCENTAGE = 200  # 200% markup
FREE_ITEM_RATIO = 3      # Buy 3 get 1 free policy

def display_products_readonly(inventory):
    """Display available products with perfect alignment"""
    print("\n=== Available Products ===")
    # Column widths
    cols = {
        'num': 4,
        'brand': 12,
        'product': 22,  # Increased to accommodate long names
        'origin': 12,
        'stock': 6,
        'cost': 12
    }
    
    # Header
    header = (f"{'No.':<{cols['num']}} "
              f"{'Brand':<{cols['brand']}} "
              f"{'Product':<{cols['product']}} "
              f"{'Origin':<{cols['origin']}} "
              f"{'Stock':<{cols['stock']}} "
              f"{'Cost':<{cols['cost']}}")
    print(header)
    print("-" * len(header))
    
    # Rows
    for idx, product in enumerate(inventory, 1):
        # Truncate product name if too long
        product_name = (product['Product'][:cols['product']-3] + '...' 
                       if len(product['Product']) > cols['product'] 
                       else product['Product'])
        
        print(f"{idx:<{cols['num']}} "
              f"{product['Brand']:<{cols['brand']}} "
              f"{product_name:<{cols['product']}} "
              f"{product['Country']:<{cols['origin']}} "
              f"{product['Stock']:<{cols['stock']}} "
              f"{product['Cost Price']:>6} rupees")

def display_products(inventory):
    """Display full product details (for sales/restocking)"""
    print("\n=== Product Details ===")
    print(f"{'No.':<4} {'Brand':<12} {'Product':<22} {'Origin':<12} {'Stock':<6} {'Price':<15}")
    print("-" * 80)
    for idx, product in enumerate(inventory, 1):
        print(f"{idx:<4} {product['Brand']:<12} {product['Product']:<22} "
              f"{product['Country']:<12} {product['Stock']:<6} {product['Selling Price']:.2f} rupees")

def generate_invoice(order, customer_name):
    """Generate sales invoice with transaction details including 13% VAT"""
    now = datetime.datetime.now()
    timestamp = now.strftime("%Y-%m-%d")
    filename = f"Sales_Invoice_{customer_name}_{timestamp}.txt"
    vat_percentage = 13
    
    # Calculate items and totals
    items_table = []
    total_net = total_vat = 0
    
    for idx, (product, details) in enumerate(order.items(), 1):
        quantity = details['quantity']
        unit_price = details['price']
        net_amount = quantity * unit_price
        vat_amount = net_amount * vat_percentage / 100
        gross_amount = net_amount + vat_amount
        
        total_net += net_amount
        total_vat += vat_amount
        
        items_table.append(
            f"| {idx:>2} | {product[:20]:<20} | {quantity:>4} | {unit_price:>10.2f} | "
            f"{vat_percentage:>5}% | {net_amount:>10.2f} | {vat_amount:>10.2f} | "
            f"{gross_amount:>12.2f} |"
        )
    
    # Generate invoice template
    invoice_template = f"""

{'VAT Invoice'.center(64)}    
===============================================================
{'WeCare'.center(64)}
{'123 Lakeside Road'.center(64)}
{'Pokhara, Lakeside-6, Nepal'.center(64)}
===============================================================
{'Email address: wecarepvtltd@gmail.com'.ljust(32)}{'Contact No: 9800000000'.rjust(32)}
===============================================================
{'Invoice Number: INV-' + timestamp:<32}{'Sales Date: ' + now.strftime('%Y-%m-%d'):>32}
{'VAT Number: VAT123456789':<32}{'Bill Issue Date: ' + now.strftime('%Y-%m-%d'):>32}

--------------------------------------------------------------
Buyer's Name: {customer_name}
Buyer's Address: [Customer Address]
{'Buyer\'s VAT Number: [Customer VAT Number]':<32}{'Buyer\'s Contact No: [Customer Contact No]':>32}

--------------------------------------------------------------
| No. | Description         | Qty | Unit Price | VAT %  | Net Amount | VAT Amount | Grand Total |
--------------------------------------------------------------
{"\n".join(items_table)}
--------------------------------------------------------------
Subtotal: {total_net:>45.2f} rupees
Total VAT: {total_vat:>45.2f} rupees
Total Amount Due: {(total_net + total_vat):>38.2f} rupees

Bank Account Details:
Account Name: WeCare Pvt. Ltd.
Account Number: 1234567890
Bank Name: Nabil Bank
Branch: Lakeside Branch, Pokhara

===============================================================
{'Thank you for your business!'.center(64)}
===============================================================
"""
    
    with open(filename, 'w') as file:
        file.write(invoice_template)
    
    print(f"\nVAT Invoice generated: {filename}")

def process_restock(inventory):
    """Handle product restocking with optional cost price updates"""
    display_products(inventory)
    restock_items = []
    
    # First get supplier name
    supplier_name = input("Enter supplier name (or leave blank to cancel): ").strip()
    if not supplier_name:
        print("Restock cancelled.")
        return
    
    while True:
        try:
            choice = input("\nEnter product number to restock (0 to finish): ").strip()
            if not choice.isdigit():
                print("Please enter a valid number")
                continue

            choice = int(choice)
            if choice == 0:
                if not restock_items:
                    print("Restock cancelled - no items were added")
                    return
                break

            if choice < 1 or choice > len(inventory):
                print("Invalid product number")
                continue

            product = inventory[choice - 1]
            qty = input(f"Enter restock quantity for {product['Product']} (0 to cancel): ").strip()
            if not qty.isdigit():
                print("Please enter a valid quantity")
                continue

            qty = int(qty)
            if qty < 0:
                print("Quantity cannot be negative")
                continue
            if qty == 0:
                print("Product restock cancelled")
                continue

            # Display current cost price and allow updates
            print(f"Current cost price: {product['Cost Price']} rupees")
            new_price = input("Enter new cost price (leave blank to keep current): ").strip()

            if new_price:
                try:
                    new_price = float(new_price)
                    if new_price <= 0:
                        print("Cost price must be positive")
                        continue
                    product['Cost Price'] = new_price
                    print(f"Updated cost price to {new_price} rupees")
                except ValueError:
                    print("Invalid price. Using current price.")

            restock_items.append({
                'product': product['Product'],
                'brand': product['Brand'],
                'country': product['Country'],
                'quantity': qty,
                'cost_price': product['Cost Price']
            })

            inventory[choice - 1]['Stock'] += qty
            print(f"Restocked {qty} {product['Product']}")

        except Exception as e:
            print(f"Error: {e}")

    # Generate restock invoice only if items were added
    timestamp = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    filename = f"Restock_Invoice_{supplier_name}_{timestamp}.txt"
    total_amount = sum(item['quantity'] * item['cost_price'] for item in restock_items)

    with open(filename, 'w') as file:
        file.write("=== WE CARE BEAUTY - RESTOCK INVOICE ===\n\n")
        file.write(f"Date: {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        file.write(f"Supplier: {supplier_name}\n")
        file.write("=" * 90 + "\n")
        file.write(f"{'Product':<20} {'Brand':<10} {'Origin':<12} {'Qty':<5} {'Unit Cost':<15} {'Subtotal':<15}\n")
        file.write("-" * 90 + "\n")

        for item in restock_items:
            subtotal = item['quantity'] * item['cost_price']
            file.write(f"{item['product']:<20} {item['brand']:<10} {item['country']:<12} "
                      f"{item['quantity']:<5} {item['cost_price']:<15} {subtotal:<15}\n")

        file.write("=" * 70 + "\n")
        file.write(f"TOTAL AMOUNT: {total_amount} rupees\n")
        file.write("=" * 70 + "\n")

    print(f"\nRestock invoice generated: {filename}")
    update_inventory_file("inventory.txt", inventory)
    print("Inventory updated successfully!")

def process_sale(inventory):
    """Handle customer sale transaction with buy-3-get-1-free"""
    display_products(inventory)
    order = {}
    
    while True:
        try:
            choice = input("\nEnter product number (0 to finish): ").strip()
            if not choice.isdigit():
                print("Please enter a valid number")
                continue
                
            choice = int(choice)
            if choice == 0:
                if not order:
                    print("Returning to main menu...")
                    return
                break
                
            if choice < 1 or choice > len(inventory):
                print("Invalid product number")
                continue
                
            product = inventory[choice-1]
            max_qty = product['Stock']
            
            if max_qty <= 0:
                print(f"Sorry, {product['Product']} is out of stock")
                continue
                
            qty = input(f"Enter quantity for {product['Product']} (max {max_qty}): ").strip()
            if not qty.isdigit():
                print("Please enter a valid quantity")
                continue
                
            qty = int(qty)
            if qty <= 0:
                print("Quantity must be positive")
                continue
                
            # Apply buy-3-get-1-free
            free_items = calculate_free_items(qty)
            total_items = qty + free_items
            
            if total_items > max_qty:
                print(f"Not enough stock. Only {max_qty} available")
                continue
                
            # Add to order
            if product['Product'] in order:
                order[product['Product']]['quantity'] += qty
            else:
                order[product['Product']] = {
                    'quantity': qty,
                    'price': product['Selling Price'],
                    'brand': product['Brand']
                }
            
            # Update stock (deduct purchased + free items)
            inventory[choice-1]['Stock'] -= total_items
            print(f"Added {qty} {product['Product']} (+{free_items} free)")
            
        except Exception as e:
            print(f"Error: {e}")
    
    # Only proceed with invoice if products were selected
    if order:
        customer_name = input("\nEnter customer name: ").strip()
        generate_invoice(order, customer_name)
        update_inventory_file("inventory.txt", inventory)
        print("Sale completed and stock updated!")