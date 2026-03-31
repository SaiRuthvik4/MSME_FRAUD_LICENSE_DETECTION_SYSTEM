import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import ApplicantTable from "./ApplicantTable";
import api from "../services/api"; // ✅ use api instance

const Dashboard = () => {
  const [applicants, setApplicants] = useState([]);

  const fetchApplicants = async () => {
    try {
      const res = await api.get("/api/applicants"); // ✅ correct call
      setApplicants(res.data);
    } catch (error) {
      console.error("Error fetching applicants:", error);
    }
  };

  useEffect(() => {
    fetchApplicants();
  }, []);

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