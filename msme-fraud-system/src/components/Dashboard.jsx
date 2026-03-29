// Dashboard.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import ApplicantTable from "./ApplicantTable";
import { motion } from "framer-motion";

const Dashboard = () => {
  const [applicants, setApplicants] = useState([]);

  const fetchApplicants = async () => {
    const res = await axios.get("http://localhost:8080/api/applicants");
    setApplicants(res.data);
  };

  useEffect(() => { fetchApplicants(); }, []);

  return (
    <motion.div
      className="table-container"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 1 }}
    >
      <h2>All Applicants</h2>
      <ApplicantTable applicants={applicants} />
    </motion.div>
  );
};

export default Dashboard;