INSERT INTO categories (name, category_related) VALUES
    ('Breakfast', NULL),
    ('Lunch/Dinner', NULL),
    ('Snacks', NULL),
    ('Drinks', NULL),
    ('Baked Goods', 1),
    ('Fruits', 1),
    ('Dairy', 1),
    ('Main Dishes', 2),
    ('Salads', 2),
    ('Desserts', 2),
    ('Savory', 3),
    ('Sweets', 3),
    ('Alcoholic Beverages', 4),
    ('Non-Alcoholic Drinks', 4),
    ('Hot Beverages', 4);

INSERT INTO products (name, price, category_id, image, stock) VALUES
    ('Croissant', 500, 5, 'croissant.jpg', 50),
    ('Toast with Butter', 400, 5, 'toast.jpg', 40),
    ('Butter Croissant', 550, 5, 'butter_croissant.jpg', 30),
    ('Fresh Apple', 300, 6, 'apple.jpg', 60),
    ('Fruit Mix', 450, 6, 'fruit_mix.jpg', 25),
    ('Banana', 250, 6, 'banana.jpg', 35),
    ('Plain Yogurt', 350, 7, 'yogurt.jpg', 20),
    ('Chocolate Milk', 380, 7, 'chocolate_milk.jpg', 15),
    ('Fresh Cheese', 450, 7, 'cheese.jpg', 18),
    ('Pasta with Sauce', 1200, 8, 'pasta.jpg', 20),
    ('Chicken with Rice', 1300, 8, 'chicken_rice.jpg', 25),
    ('Beef with Mashed Potatoes', 1400, 8, 'beef_mashed.jpg', 22),
    ('Caesar Salad', 800, 9, 'caesar.jpg', 30),
    ('Mixed Salad', 700, 9, 'mixed_salad.jpg', 35),
    ('Quinoa Salad', 850, 9, 'quinoa.jpg', 20),
    ('Flan with Caramel', 500, 10, 'flan.jpg', 25),
    ('Vanilla Ice Cream', 600, 10, 'ice_cream.jpg', 30),
    ('Chocolate Mousse', 650, 10, 'mousse.jpg', 28),
    ('Potato Chips', 300, 11, 'chips.jpg', 40),
    ('Nachos with Cheese', 450, 11, 'nachos.jpg', 35),
    ('Salted Peanuts', 280, 11, 'peanuts.jpg', 50),
    ('Cereal Bar', 200, 12, 'cereal_bar.jpg', 50),
    ('Brownie', 350, 12, 'brownie.jpg', 40),
    ('Chocolate Cookies', 300, 12, 'cookies.jpg', 45),
    ('Red Wine', 900, 13, 'wine.jpg', 15),
    ('Beer', 600, 13, 'beer.jpg', 25),
    ('Whisky', 1200, 13, 'whisky.jpg', 10),
    ('Mineral Water', 200, 14, 'water.jpg', 60),
    ('Orange Juice', 300, 14, 'orange_juice.jpg', 45),
    ('Cola Soda', 350, 14, 'cola.jpg', 50),
    ('Coffee', 300, 15, 'coffee.jpg', 40),
    ('Black Tea', 250, 15, 'tea.jpg', 35),
    ('Coffee with Milk', 350, 15, 'coffee_milk.jpg', 30);