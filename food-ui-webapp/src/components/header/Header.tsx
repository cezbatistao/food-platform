const Header = () => {
  return (
    <div className="container">
    <header className="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
      <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
        <i style={{ paddingRight: "15px" }} className="fas fa-utensils fa-2x"></i>
        <span className="fs-4">Food Platform</span>
      </a>
      <ul className="nav nav-pills">
        <li className="nav-item">
          <i className="fas fa-shopping-basket fa-2x"></i>
        </li>
      </ul>
    </header>
  </div>
  );
}

export default Header;