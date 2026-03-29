// ApplicantTable.jsx
import React from "react";
import { motion } from "framer-motion";

const ApplicantTable = ({ applicants }) => {
  const getScoreClass = (score) => {
    if (score < 40) return "low";
    if (score < 80) return "medium";
    return "high";
  };

  return (
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Mobile</th>
          <th>Business</th>
          <th>PAN</th>
          <th>Aadhaar</th>
          <th>GST</th>
          <th>Fraud Score</th>
          <th>Fraud Reason</th>
        </tr>
      </thead>
      <tbody>
        {applicants.map((app) => (
          <motion.tr key={app.id} initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.5 }}>
            <td>{app.name}</td>
            <td>{app.mobile}</td>
            <td>{app.businessName}</td>
            <td>{app.panNumber}</td>
            <td>{app.aadhaarNumber}</td>
            <td>{app.gstNumber}</td>
            <td className={`fraud-score ${getScoreClass(app.fraudScore)}`}>{app.fraudScore}</td>
            <td>{app.fraudReason}</td>
          </motion.tr>
        ))}
      </tbody>
    </table>
  );
};

export default ApplicantTable;