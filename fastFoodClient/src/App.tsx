import './App.css';
import { useEffect, useState } from 'react';

const featuredItems = [
  { name: 'Cheeseburger', price: '$4.99', img: 'https://i.imgur.com/5Aqgz7o.png' },
  { name: 'Fries', price: '$2.49', img: 'https://i.imgur.com/1bX5QH6.png' },
  { name: 'Soda', price: '$1.99', img: 'https://i.imgur.com/8Km9tLL.png' },
];

const menuItems = [
  { name: 'Chicken Nuggets', price: '$3.99' },
  { name: 'Veggie Burger', price: '$5.49' },
  { name: 'Milkshake', price: '$2.99' },
  { name: 'Salad', price: '$3.49' },
];

function App() {
  const [allItems, setAllItems] = useState<{ name: string; price: string; img?: string }[]>([]);

  useEffect(() => {
    // Combine featured and menu items for the chart
    const combinedItems = [...featuredItems, ...menuItems];
    setAllItems(combinedItems);
  }, []);

  return (
    <div className="fastfood-home">
      <header className="header">
        <h1>🍔 FastFood Express</h1>
        <p>Delicious food, fast delivery!</p>
      </header>

      <section className="featured">
        <h2>Featured Items</h2>
        <div className="featured-list">
          {featuredItems.map(item => (
            <div className="featured-item" key={item.name}>
              <img src={item.img} alt={item.name} className="featured-img" />
              <h3>{item.name}</h3>
              <p>{item.price}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="menu">
        <h2>Menu</h2>
        <ul>
          {menuItems.map(item => (
            <li key={item.name}>
              <span>{item.name}</span>
              <span>{item.price}</span>
            </li>
          ))}
        </ul>
      </section>

      <section className="price-chart">
        <h2>Price Overview</h2>
        {/* Chart will be rendered here */}
      </section>
    </div>
  );
}

export default App;