import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import AddApplicant from "./components/AddApplicant";
import Dashboard from "./components/Dashboard";
import "./App.css";

function App() {
  return (
    <Router>
      <div className="app">
        <nav className="navbar">
          <h1>MSME Fraud Detection</h1>
          <div className="nav-links">
            <Link to="/">Add Applicant</Link>
            <Link to="/dashboard">Dashboard</Link>
          </div>
        </nav>

        <Routes>
          <Route path="/" element={<AddApplicant />} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;