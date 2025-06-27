def update_inventory_file(file_path, inventory):
    """Update the inventory text file with current stock and prices"""
    with open(file_path, 'w') as file:
        file.write("Brand,Product Name,Country of Origin,Quantity,Cost Price in rupees\n")
        for product in inventory:
            file.write(f"{product['Brand']},{product['Product']},{product['Country']},"
                      f"{product['Stock']},{product['Cost Price']}\n")