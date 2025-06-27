from operations import display_products_readonly, process_sale, process_restock
from read import read_inventory

def main():
    inventory = read_inventory("inventory.txt")
    
    while True:
        print("\n=== WeCare Beauty - INVENTORY SYSTEM ===")
        print("1. View Available Products")
        print("2. Process Sale (Buy 3 Get 1 Free)")
        print("3. Process Restock")
        print("4. Ex3" 
        "it")
        
        choice = input("Enter your choice (1-4): ")
        
        if choice == '1':
            display_products_readonly(inventory)
        elif choice == '2':
            process_sale(inventory)
        elif choice == '3':
            process_restock(inventory)
        elif choice == '4':
            print("Thank you for using We Care Beauty Inventory System!")
            break
        else:
            print("Invalid choice. Please enter 1-4.")

if __name__ == "__main__":
    main()